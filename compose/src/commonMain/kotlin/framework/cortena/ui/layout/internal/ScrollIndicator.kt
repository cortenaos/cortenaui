/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.layout.internal

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import framework.cortena.ui.geometry.Orientation
import framework.cortena.ui.layout.ScrollIndicatorPosition
import framework.cortena.ui.motion.LocalMotion
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Source-agnostic snapshot of a scroll position. The four CortenaUI scrollable (`ScrollView`,
 * `LazyScrollView`, `GridView`, `LazyGridView`) project their underlying state into this so the
 * shared indicator UI doesn't need to know about `ScrollState`, `LazyListState`, or
 * `LazyGridState`.
 * - [viewportPx] — visible viewport along the scroll axis, in pixels.
 * - [totalContentPx] — total scrollable content along the same axis. Equals viewport + maxScroll.
 * - [scrollFraction] — `0f` at start, `1f` at end. Used to position the indicator. Clamped here to
 *   the same range so callers can be lazy with their math.
 * - [scrollByFraction] — applies a scroll delta expressed as a fraction of the total scrollable
 *   range. Called by the drag-to-scrub gesture. Returns immediately; the implementation may
 *   coroutine-launch the actual scroll on its own scope.
 */
internal class ScrollIndicatorMetrics(
    val viewportPx: Float,
    val totalContentPx: Float,
    val scrollFraction: Float,
    val scrollByFraction: (Float) -> Unit,
) {
    val maxScrollPx: Float
        get() = (totalContentPx - viewportPx).coerceAtLeast(0f)

    val hasOverflow: Boolean
        get() = maxScrollPx > 0f
}

/**
 * Auto-hiding, drag-to-scrub scroll indicator shared across CortenaUI scrollable layouts.
 *
 * Behavior:
 * - Position is mapped 1:1 to scroll — no spring smoothing, otherwise the indicator visibly lags
 *   behind the content on fast scrolls.
 * - Auto-hide alpha tween fades after [IndicatorIdleHideDelayMillis] of inactivity.
 * - When the user drags the indicator directly, thickness scales up by [IndicatorDragScale] with a
 *   slight spring overshoot, confirming the active interaction.
 *
 * Position math is derived from [ScrollIndicatorMetrics] so the same composable serves linear
 * scroll states and grid scroll states without modification.
 */
@Suppress("FrequentlyChangingValue")
@Composable
internal fun ScrollIndicator(
    metrics: ScrollIndicatorMetrics,
    orientation: Orientation,
    thickness: Dp,
    color: Color,
    shape: Shape,
    padding: Dp,
    position: ScrollIndicatorPosition,
    autoHide: Boolean,
    draggable: Boolean,
    activitySignal: Any,
) {
    if (!metrics.hasOverflow) return

    val density = LocalDensity.current
    val motion = LocalMotion.current
    val minIndicatorSize = with(density) { 48.dp.toPx() }

    val viewportSize = metrics.viewportPx
    val totalSize = metrics.totalContentPx
    val indicatorRatio = if (totalSize > 0f) viewportSize / totalSize else 1f

    val alignment =
        when (orientation) {
            Orientation.Vertical ->
                when (position) {
                    ScrollIndicatorPosition.End -> Alignment.TopEnd
                    ScrollIndicatorPosition.Start -> Alignment.TopStart
                }

            Orientation.Horizontal ->
                when (position) {
                    ScrollIndicatorPosition.End -> Alignment.BottomStart
                    ScrollIndicatorPosition.Start -> Alignment.TopStart
                }
        }

    val indicatorSizePx = (viewportSize * indicatorRatio).coerceAtLeast(minIndicatorSize)
    val trackSizePx = (viewportSize - indicatorSizePx).coerceAtLeast(0f)
    val targetOffsetPx = metrics.scrollFraction.coerceIn(0f, 1f) * trackSizePx

    val scope = rememberCoroutineScope()
    var isDragging by remember { mutableStateOf(false) }
    val isDraggingState = rememberUpdatedState(isDragging)

    // Thickness scale animates up to IndicatorDragScale while the user is dragging the indicator,
    // confirming the active interaction with a slight spring overshoot. We borrow stiffness from
    // motion.smooth and override damping to a bouncier value — overshoot is the whole point here,
    // so it justifies a custom dampingRatio rather than a stock preset.
    val thicknessScale = remember { Animatable(1f) }
    LaunchedEffect(isDragging) {
        thicknessScale.animateTo(
            targetValue = if (isDragging) IndicatorDragScale else 1f,
            animationSpec =
                spring(
                    stiffness = motion.smooth.stiffness,
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                ),
        )
    }

    // Auto-hide. Visible whenever the activity signal changes or while dragging. Fades out after
    // a short idle period. Snapped to 1f on activity so the response feels immediate.
    val alphaAnim = remember { Animatable(if (autoHide) 0f else 1f) }
    if (autoHide) {
        LaunchedEffect(activitySignal, isDragging) {
            snapshotFlow { activitySignal to isDragging }
                .collectLatest { _ ->
                    scope.launch { alphaAnim.snapTo(1f) }
                    if (!isDraggingState.value) {
                        delay(IndicatorIdleHideDelayMillis)
                        if (!isDraggingState.value) {
                            alphaAnim.animateTo(
                                targetValue = 0f,
                                animationSpec =
                                    tween(
                                        durationMillis = motion.medium,
                                        easing = motion.standardEasing,
                                    ),
                            )
                        }
                    }
                }
        }
    }

    // Drag-to-scrub. Tap and drag on the indicator maps pointer delta along the track to a scroll
    // fraction, which the metrics owner applies to its underlying state.
    val dragModifier =
        if (draggable && trackSizePx > 0f) {
            Modifier.pointerInput(orientation, trackSizePx) {
                detectDragGestures(
                    onDragStart = {
                        isDragging = true
                        scope.launch { alphaAnim.snapTo(1f) }
                    },
                    onDragEnd = { isDragging = false },
                    onDragCancel = { isDragging = false },
                    onDrag = { _, dragAmount ->
                        val deltaPx =
                            if (orientation == Orientation.Vertical) dragAmount.y else dragAmount.x
                        val deltaFraction = deltaPx / trackSizePx
                        if (deltaFraction != 0f) {
                            metrics.scrollByFraction(deltaFraction)
                        }
                    },
                )
            }
        } else {
            Modifier
        }

    val baseIndicatorModifier =
        if (orientation == Orientation.Vertical) {
            Modifier.padding(padding)
                .width(thickness * thicknessScale.value)
                .height(with(density) { indicatorSizePx.toDp() })
                .graphicsLayer {
                    translationY = targetOffsetPx
                    alpha = alphaAnim.value
                }
        } else {
            Modifier.padding(padding)
                .height(thickness * thicknessScale.value)
                .width(with(density) { indicatorSizePx.toDp() })
                .graphicsLayer {
                    translationX = targetOffsetPx
                    alpha = alphaAnim.value
                }
        }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = alignment) {
        Box(
            modifier = baseIndicatorModifier.clip(shape).background(color, shape).then(dragModifier)
        )
    }
}

private const val IndicatorIdleHideDelayMillis: Long = 1500L
private const val IndicatorDragScale: Float = 2f
