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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
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
 * Lazy 2D grid backed by `LazyVerticalGrid` / `LazyHorizontalGrid`. Cells are described through a
 * [LazyGridScope] DSL (`item { }`, `items(list) { }`) and only those visible in the viewport are
 * composed.
 *
 * Inherits CortenaUI's bounce overscroll, auto-hiding indicator with drag-to-scrub, and threshold
 * auto-release. Column / row layout is defined by [GridColumns] — choose between a
 * [GridColumns.Fixed] count or [GridColumns.Adaptive] minimum cell size.
 *
 * For [Orientation.Vertical] the grid scrolls vertically and [columns] controls horizontal cell
 * count; for [Orientation.Horizontal] it scrolls horizontally and [columns] controls vertical row
 * count instead.
 */
@Suppress("FrequentlyChangingValue")
@Composable
fun LazyGridView(
    columns: GridColumns,
    modifier: Modifier = Modifier,

    // Orientation
    orientation: Orientation = Orientation.Vertical,

    // Scroll Control
    state: LazyGridState = rememberLazyGridState(),
    enabled: Boolean = true,
    reverseLayout: Boolean = false,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),

    // Content
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,

    // Scroll Indicator
    showScrollIndicator: Boolean = true,
    indicatorThickness: Dp = 3.dp,
    indicatorColor: Color = Color.Unspecified,
    indicatorShape: Shape = CapsuleShape(),
    indicatorPadding: Dp = 2.dp,
    indicatorPosition: ScrollIndicatorPosition = ScrollIndicatorPosition.End,
    autoHideIndicator: Boolean = true,
    draggableIndicator: Boolean = true,
    content: LazyGridScope.() -> Unit,
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

    val cells = columns.toGridCells()
    val indicatorScope = rememberCoroutineScope()

    Box(modifier = safeModifier) {
        if (orientation == Orientation.Vertical) {
            LazyVerticalGrid(
                columns = cells,
                modifier = Modifier.clipToBounds().then(overscrollEffect.overscroll),
                state = state,
                contentPadding = contentPadding,
                reverseLayout = reverseLayout,
                userScrollEnabled = enabled,
                verticalArrangement = verticalArrangement,
                horizontalArrangement = horizontalArrangement,
                flingBehavior = flingBehavior,
                overscrollEffect = overscrollEffect,
                content = content,
            )
        } else {
            LazyHorizontalGrid(
                rows = cells,
                modifier = Modifier.clipToBounds().then(overscrollEffect.overscroll),
                state = state,
                contentPadding = contentPadding,
                reverseLayout = reverseLayout,
                userScrollEnabled = enabled,
                horizontalArrangement = horizontalArrangement,
                verticalArrangement = verticalArrangement,
                flingBehavior = flingBehavior,
                overscrollEffect = overscrollEffect,
                content = content,
            )
        }

        if (showScrollIndicator) {
            val metricsState =
                remember(state) {
                    derivedStateOf {
                        deriveLazyGridMetrics(state) { fraction ->
                            indicatorScope.launch { scrollLazyGridByFraction(state, fraction) }
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

private fun GridColumns.toGridCells(): GridCells =
    when (this) {
        is GridColumns.Fixed -> GridCells.Fixed(count)
        is GridColumns.Adaptive -> GridCells.Adaptive(minSize)
    }

private fun deriveLazyGridMetrics(
    state: LazyGridState,
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

    // Group visible items by row (vertical grid) or column (horizontal grid). Each line shares
    // the same cross-axis index, so we use that as the grouping key.
    val visibleLines = visible.groupBy { it.row.coerceAtLeast(it.column) }
    val averageLineSize =
        visibleLines.values
            .map { line -> line.maxOf { it.size.height.coerceAtLeast(it.size.width) } }
            .average()
            .toFloat()
            .coerceAtLeast(1f)

    val itemsPerLine = (visible.size.toFloat() / visibleLines.size).coerceAtLeast(1f)
    val totalLines = (totalItems / itemsPerLine).toInt().coerceAtLeast(1)
    val totalContentPx = (averageLineSize * totalLines).coerceAtLeast(viewportPx)

    val firstVisible = visible.first()
    val firstVisibleAbsoluteOffset =
        (firstVisible.row.coerceAtLeast(firstVisible.column) * averageLineSize) -
            firstVisible.offset.y.coerceAtLeast(firstVisible.offset.x).toFloat()
    val maxScroll = (totalContentPx - viewportPx).coerceAtLeast(1f)
    val scrollFraction = (firstVisibleAbsoluteOffset / maxScroll).coerceIn(0f, 1f)

    return ScrollIndicatorMetrics(
        viewportPx = viewportPx,
        totalContentPx = totalContentPx,
        scrollFraction = scrollFraction,
        scrollByFraction = scrollByFraction,
    )
}

private suspend fun scrollLazyGridByFraction(state: LazyGridState, fraction: Float) {
    val info = state.layoutInfo
    val visible = info.visibleItemsInfo
    if (visible.isEmpty() || info.totalItemsCount == 0) return

    val visibleLines = visible.groupBy { it.row.coerceAtLeast(it.column) }
    val averageLineSize =
        visibleLines.values
            .map { line -> line.maxOf { it.size.height.coerceAtLeast(it.size.width) } }
            .average()
            .toFloat()
            .coerceAtLeast(1f)

    val itemsPerLine = (visible.size.toFloat() / visibleLines.size).coerceAtLeast(1f)
    val totalLines = (info.totalItemsCount / itemsPerLine).toInt().coerceAtLeast(1)
    val viewportPx = (info.viewportEndOffset - info.viewportStartOffset).toFloat()
    val totalContentPx = (averageLineSize * totalLines).coerceAtLeast(viewportPx)
    val deltaPx = fraction * (totalContentPx - viewportPx)

    if (abs(deltaPx) < 1f) return
    state.scrollBy(deltaPx)
}
