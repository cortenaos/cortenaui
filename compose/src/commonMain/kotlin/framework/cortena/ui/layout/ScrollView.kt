/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.layout

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import framework.cortena.ui.geometry.Orientation
import framework.cortena.ui.layout.internal.BounceOverscrollEffect
import framework.cortena.ui.shape.CapsuleShape
import framework.cortena.ui.theme.LocalColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class ScrollIndicatorPosition {
    Start,
    End,
}

@Composable
fun ScrollView(
    modifier: Modifier = Modifier,

    // Orientation
    orientation: Orientation = Orientation.Vertical,

    // Scroll Control
    scrollState: ScrollState = rememberScrollState(),
    enabled: Boolean = true,
    reverseLayout: Boolean = false,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),

    // Content Padding
    contentPadding: PaddingValues = PaddingValues(0.dp),

    // Scroll Indicator
    showScrollIndicator: Boolean = true,
    indicatorThickness: Dp = 3.dp,
    indicatorColor: Color = Color.Unspecified,
    indicatorShape: Shape = CapsuleShape(),
    indicatorPadding: Dp = 2.dp,
    indicatorPosition: ScrollIndicatorPosition = ScrollIndicatorPosition.End,
    autoHideIndicator: Boolean = true,
    draggableIndicator: Boolean = true,

    // Callbacks
    onScrolled: ((scrollValue: Int, maxScrollValue: Int) -> Unit)? = null,
    onReachedTop: (() -> Unit)? = null,
    onReachedBottom: (() -> Unit)? = null,

    // Sticky Header
    // TODO: Implement sticky header for ScrollView
    // stickyHeader: (@Composable () -> Unit)? = null,

    content: @Composable () -> Unit,
) {
    val safeModifier =
        modifier.then(
            if (orientation == Orientation.Vertical) {
                Modifier.heightIn(min = 48.dp) // minimum sensible height
            } else {
                Modifier.widthIn(min = 48.dp)
            }
        )

    val colors = LocalColors.current
    val resolvedIndicatorColor =
        if (indicatorColor.isSpecified) indicatorColor else Color(colors.outline)

    // Bounce Overscroll
    val bounceScope = rememberCoroutineScope()
    val overscrollEffect =
        remember(bounceScope, orientation) { BounceOverscrollEffect(bounceScope, orientation) }

    // Callbacks via SnapshotFlow
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { value ->
                onScrolled?.invoke(value, scrollState.maxValue)
                if (value == 0) onReachedTop?.invoke()
                if (value == scrollState.maxValue && scrollState.maxValue > 0) {
                    onReachedBottom?.invoke()
                }
            }
    }

    // Layout
    Box(modifier = safeModifier) {
        val scrollModifier =
            if (orientation == Orientation.Vertical) {
                Modifier.verticalScroll(
                    state = scrollState,
                    enabled = enabled,
                    flingBehavior = flingBehavior,
                    reverseScrolling = reverseLayout,
                    overscrollEffect = overscrollEffect,
                )
            } else {
                Modifier.horizontalScroll(
                    state = scrollState,
                    enabled = enabled,
                    flingBehavior = flingBehavior,
                    reverseScrolling = reverseLayout,
                    overscrollEffect = overscrollEffect,
                )
            }

        if (orientation == Orientation.Vertical) {
            Column(
                modifier = scrollModifier.then(overscrollEffect.overscroll).padding(contentPadding)
            ) {
                content()
            }
        } else {
            Row(
                modifier = scrollModifier.then(overscrollEffect.overscroll).padding(contentPadding)
            ) {
                content()
            }
        }

        // Scroll Indicator
        if (showScrollIndicator && scrollState.maxValue > 0) {
            ScrollIndicator(
                scrollState = scrollState,
                orientation = orientation,
                thickness = indicatorThickness,
                color = resolvedIndicatorColor,
                shape = indicatorShape,
                padding = indicatorPadding,
                position = indicatorPosition,
                autoHide = autoHideIndicator,
                draggable = draggableIndicator,
            )
        }
    }
}

@Suppress("FrequentlyChangingValue")
@Composable
private fun ScrollIndicator(
    scrollState: ScrollState,
    orientation: Orientation,
    thickness: Dp,
    color: Color,
    shape: Shape,
    padding: Dp,
    position: ScrollIndicatorPosition,
    autoHide: Boolean,
    draggable: Boolean,
) {
    val density = LocalDensity.current
    val minIndicatorSize = with(density) { 48.dp.toPx() }
    val viewportSize = scrollState.viewportSize.toFloat()
    val totalSize = viewportSize + scrollState.maxValue
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

    val scrollFraction =
        if (scrollState.maxValue > 0) {
            scrollState.value.toFloat() / scrollState.maxValue
        } else {
            0f
        }

    val indicatorSizePx =
        (viewportSize * indicatorRatio).coerceAtLeast(minIndicatorSize)
    val trackSizePx = (viewportSize - indicatorSizePx).coerceAtLeast(0f)
    val targetOffsetPx = scrollFraction * trackSizePx

    // Indicator position is mapped 1:1 to scroll. We deliberately do NOT spring-smooth this — any
    // easing here makes the indicator visibly lag behind the content during fast scrolls.
    val scope = rememberCoroutineScope()
    var isDragging by remember { mutableStateOf(false) }
    val isDraggingState = rememberUpdatedState(isDragging)

    // Thickness scale animates up to IndicatorDragScale while the user is dragging the indicator,
    // confirming the active interaction with a slight spring overshoot.
    val thicknessScale = remember { Animatable(1f) }
    LaunchedEffect(isDragging) {
        thicknessScale.animateTo(
            targetValue = if (isDragging) IndicatorDragScale else 1f,
            animationSpec =
                spring(
                    stiffness = Spring.StiffnessMediumLow,
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                ),
        )
    }

    // Auto-hide. Visible whenever the scroll value or drag state is active. Fades out after a
    // short idle period. Pulled to 1f instantly on activity so the response feels immediate.
    val alphaAnim = remember { Animatable(if (autoHide) 0f else 1f) }
    if (autoHide) {
        LaunchedEffect(scrollState, isDragging) {
            snapshotFlow { scrollState.value to isDragging }
                .collectLatest { _ ->
                    // Show
                    scope.launch {
                        alphaAnim.snapTo(1f)
                    }
                    // Schedule hide unless dragging
                    if (!isDraggingState.value) {
                        delay(IndicatorIdleHideDelayMillis)
                        if (!isDraggingState.value) {
                            alphaAnim.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = IndicatorFadeDurationMillis),
                            )
                        }
                    }
                }
        }
    }

    // Drag-to-scroll. Tap and drag on the indicator translates pointer delta along the track and
    // applies it to scrollState. Map ratio: trackSizePx → scrollState.maxValue.
    val dragModifier =
        if (draggable && trackSizePx > 0f) {
            Modifier.pointerInput(orientation, trackSizePx, scrollState) {
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
                        val ratio = scrollState.maxValue.toFloat() / trackSizePx
                        val scrollDelta = (deltaPx * ratio).toInt()
                        if (scrollDelta != 0) {
                            scope.launch {
                                scrollState.scrollTo(
                                    (scrollState.value + scrollDelta).coerceIn(
                                        0,
                                        scrollState.maxValue,
                                    )
                                )
                            }
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
            modifier =
                baseIndicatorModifier
                    .clip(shape)
                    .background(color, shape)
                    .then(dragModifier)
        )
    }
}

private const val IndicatorIdleHideDelayMillis: Long = 1500L
private const val IndicatorFadeDurationMillis: Int = 220
private const val IndicatorDragScale: Float = 2f
