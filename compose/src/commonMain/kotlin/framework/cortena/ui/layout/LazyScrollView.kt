/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.layout

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import framework.cortena.ui.geometry.Orientation
import framework.cortena.ui.layout.internal.BounceOverscrollEffect
import framework.cortena.ui.layout.internal.ScrollIndicator
import framework.cortena.ui.layout.internal.ScrollIndicatorMetrics
import framework.cortena.ui.motion.LocalMotion
import framework.cortena.ui.shape.CapsuleShape
import framework.cortena.ui.theme.LocalColors
import kotlin.math.abs
import kotlinx.coroutines.launch

/**
 * Lazy scrollable container backed by `LazyColumn` / `LazyRow`. Children are described through a
 * [LazyListScope] DSL — `item { ... }`, `items(list) { ... }`, `stickyHeader { ... }` — and only
 * those visible inside the viewport are composed.
 *
 * Inherits CortenaUI's bounce overscroll, auto-hiding scroll indicator with drag-to-scrub, and
 * threshold auto-release from [ScrollView]. Supports both vertical and horizontal orientation.
 *
 * State can be hoisted via [state] for programmatic scroll, item visibility queries, or preserving
 * position across configuration changes.
 */
@Suppress("FrequentlyChangingValue")
@Composable
fun LazyScrollView(
    modifier: Modifier = Modifier,

    // Orientation
    orientation: Orientation = Orientation.Vertical,

    // Scroll Control
    state: LazyListState = rememberLazyListState(),
    enabled: Boolean = true,
    reverseLayout: Boolean = false,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),

    // Content
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,

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
    onFirstVisibleItemChanged: ((index: Int) -> Unit)? = null,
    onReachedStart: (() -> Unit)? = null,
    onReachedEnd: (() -> Unit)? = null,
    content: LazyListScope.() -> Unit,
) {
    val safeModifier =
        modifier.then(
            if (orientation == Orientation.Vertical) {
                Modifier.heightIn(min = 48.dp)
            } else {
                Modifier.widthIn(min = 48.dp)
            }
        )

    val colors = LocalColors.current
    val motion = LocalMotion.current
    val resolvedIndicatorColor =
        if (indicatorColor.isSpecified) indicatorColor else Color(colors.outline)

    val bounceScope = rememberCoroutineScope()
    val overscrollEffect =
        remember(bounceScope, orientation, motion) {
            BounceOverscrollEffect(bounceScope, orientation, motion.smooth)
        }

    if (onFirstVisibleItemChanged != null) {
        LaunchedEffect(state, onFirstVisibleItemChanged) {
            snapshotFlow { state.firstVisibleItemIndex }.collect { onFirstVisibleItemChanged(it) }
        }
    }

    if (onReachedStart != null || onReachedEnd != null) {
        LaunchedEffect(state, onReachedStart, onReachedEnd) {
            snapshotFlow {
                    val info = state.layoutInfo
                    val visible = info.visibleItemsInfo
                    val firstVisible = visible.firstOrNull()?.index ?: -1
                    val lastVisible = visible.lastOrNull()?.index ?: -1
                    Triple(firstVisible, lastVisible, info.totalItemsCount)
                }
                .collect { (first, last, total) ->
                    if (total > 0 && first == 0) onReachedStart?.invoke()
                    if (total > 0 && last == total - 1) onReachedEnd?.invoke()
                }
        }
    }

    val indicatorScope = rememberCoroutineScope()

    Box(modifier = safeModifier) {
        if (orientation == Orientation.Vertical) {
            LazyColumn(
                modifier = overscrollEffect.overscroll,
                state = state,
                contentPadding = contentPadding,
                reverseLayout = reverseLayout,
                userScrollEnabled = enabled,
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment,
                flingBehavior = flingBehavior,
                overscrollEffect = overscrollEffect,
                content = content,
            )
        } else {
            LazyRow(
                modifier = overscrollEffect.overscroll,
                state = state,
                contentPadding = contentPadding,
                reverseLayout = reverseLayout,
                userScrollEnabled = enabled,
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = verticalAlignment,
                flingBehavior = flingBehavior,
                overscrollEffect = overscrollEffect,
                content = content,
            )
        }

        if (showScrollIndicator) {
            // Lazy lists don't expose a single "scroll value" — they expose first-visible item +
            // offset. Approximate a scroll fraction by treating items as roughly equal-sized; for
            // the indicator's purpose (visual position) this is accurate enough at a 1-2 px level
            // even with variable-height items, and exact enough for drag-to-scrub via animation.
            val metricsState =
                remember(state) {
                    derivedStateOf {
                        deriveLazyListMetrics(state) { fraction ->
                            indicatorScope.launch { scrollLazyListByFraction(state, fraction) }
                        }
                    }
                }

            ScrollIndicator(
                metrics = metricsState.value,
                orientation = orientation,
                thickness = indicatorThickness,
                color = resolvedIndicatorColor,
                shape = indicatorShape,
                padding = indicatorPadding,
                position = indicatorPosition,
                autoHide = autoHideIndicator,
                draggable = draggableIndicator,
                activitySignal = state.firstVisibleItemIndex to state.firstVisibleItemScrollOffset,
            )
        }
    }
}

private fun deriveLazyListMetrics(
    state: LazyListState,
    scrollByFraction: (Float) -> Unit,
): ScrollIndicatorMetrics {
    val info = state.layoutInfo
    val viewportPx = (info.viewportEndOffset - info.viewportStartOffset).toFloat()
    val totalItems = info.totalItemsCount
    val visible = info.visibleItemsInfo

    if (totalItems == 0 || visible.isEmpty()) {
        return ScrollIndicatorMetrics(
            viewportPx = viewportPx,
            totalContentPx = viewportPx,
            scrollFraction = 0f,
            scrollByFraction = scrollByFraction,
        )
    }

    val averageItemSize = visible.sumOf { it.size }.toFloat() / visible.size.coerceAtLeast(1)
    val totalContentPx = (averageItemSize * totalItems).coerceAtLeast(viewportPx)

    val firstVisible = visible.first()
    val firstVisibleAbsoluteOffset =
        firstVisible.index * averageItemSize - firstVisible.offset.toFloat()
    val maxScroll = (totalContentPx - viewportPx).coerceAtLeast(1f)
    val scrollFraction = (firstVisibleAbsoluteOffset / maxScroll).coerceIn(0f, 1f)

    return ScrollIndicatorMetrics(
        viewportPx = viewportPx,
        totalContentPx = totalContentPx,
        scrollFraction = scrollFraction,
        scrollByFraction = scrollByFraction,
    )
}

private suspend fun scrollLazyListByFraction(state: LazyListState, fraction: Float) {
    val info = state.layoutInfo
    val visible = info.visibleItemsInfo
    if (visible.isEmpty() || info.totalItemsCount == 0) return

    val averageItemSize = visible.sumOf { it.size }.toFloat() / visible.size.coerceAtLeast(1)
    val viewportPx = (info.viewportEndOffset - info.viewportStartOffset).toFloat()
    val totalContentPx = (averageItemSize * info.totalItemsCount).coerceAtLeast(viewportPx)
    val deltaPx = fraction * (totalContentPx - viewportPx)

    if (abs(deltaPx) < 1f) return
    state.scrollBy(deltaPx)
}
