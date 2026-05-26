/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.layout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import framework.cortena.ui.geometry.Orientation
import framework.cortena.ui.layout.internal.BounceOverscrollEffect
import framework.cortena.ui.layout.internal.ScrollIndicator
import framework.cortena.ui.layout.internal.ScrollIndicatorMetrics
import framework.cortena.ui.motion.LocalMotion
import framework.cortena.ui.shape.CapsuleShape
import framework.cortena.ui.theme.LocalColors
import kotlinx.coroutines.launch

/**
 * Eager 2D grid: every cell is composed upfront, suitable for small grids where you don't need lazy
 * composition (settings dashboards, color pickers, fixed-size icon grids, etc.). For large grids
 * that benefit from item recycling, use [LazyGridView].
 *
 * Cell layout is driven by [columns]:
 * - [GridColumns.Fixed] places a fixed number of cells per cross-axis line.
 * - [GridColumns.Adaptive] computes the cell count from the available cross-axis size so each cell
 *   is at least `minSize` wide.
 *
 * Inherits CortenaUI's bounce overscroll, auto-hiding indicator with drag-to-scrub, and threshold
 * auto-release. Supports both vertical (default) and horizontal scrolling.
 *
 * Children are passed via [items], a list rendered in row-major (vertical) or column-major
 * (horizontal) order using [itemContent].
 */
@Suppress("FrequentlyChangingValue")
@Composable
fun <T> GridView(
    items: List<T>,
    columns: GridColumns,
    modifier: Modifier = Modifier,

    // Orientation
    orientation: Orientation = Orientation.Vertical,

    // Scroll Control
    scrollState: ScrollState = rememberScrollState(),
    enabled: Boolean = true,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),

    // Spacing
    contentPadding: PaddingValues = PaddingValues(0.dp),
    horizontalSpacing: Dp = 0.dp,
    verticalSpacing: Dp = 0.dp,

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
    itemContent: @Composable (index: Int, item: T) -> Unit,
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

    val indicatorScope = rememberCoroutineScope()

    Box(modifier = safeModifier) {
        BoxWithConstraints {
            val density = LocalDensity.current
            val crossAxisPx =
                if (orientation == Orientation.Vertical) constraints.maxWidth
                else constraints.maxHeight
            val crossAxisDp = with(density) { crossAxisPx.toDp() }
            val resolvedCount = resolveColumnCount(columns, crossAxisDp)

            val scrollModifier =
                if (orientation == Orientation.Vertical) {
                    Modifier.verticalScroll(
                        state = scrollState,
                        enabled = enabled,
                        flingBehavior = flingBehavior,
                        overscrollEffect = overscrollEffect,
                    )
                } else {
                    Modifier.horizontalScroll(
                        state = scrollState,
                        enabled = enabled,
                        flingBehavior = flingBehavior,
                        overscrollEffect = overscrollEffect,
                    )
                }

            if (orientation == Orientation.Vertical) {
                Column(
                    modifier =
                        scrollModifier.then(overscrollEffect.overscroll).padding(contentPadding),
                    verticalArrangement = Arrangement.spacedBy(verticalSpacing),
                ) {
                    items.chunked(resolvedCount).forEachIndexed { rowIndex, rowItems ->
                        Row(horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)) {
                            rowItems.forEachIndexed { colIndex, item ->
                                Box(modifier = Modifier.weight(1f, fill = true)) {
                                    itemContent(rowIndex * resolvedCount + colIndex, item)
                                }
                            }
                            // Pad the trailing row so cells stay correctly aligned.
                            repeat(resolvedCount - rowItems.size) {
                                Box(modifier = Modifier.weight(1f, fill = true))
                            }
                        }
                    }
                }
            } else {
                Row(
                    modifier =
                        scrollModifier.then(overscrollEffect.overscroll).padding(contentPadding),
                    horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
                ) {
                    items.chunked(resolvedCount).forEachIndexed { colIndex, colItems ->
                        Column(verticalArrangement = Arrangement.spacedBy(verticalSpacing)) {
                            colItems.forEachIndexed { rowIndex, item ->
                                Box(modifier = Modifier.weight(1f, fill = true)) {
                                    itemContent(colIndex * resolvedCount + rowIndex, item)
                                }
                            }
                            repeat(resolvedCount - colItems.size) {
                                Box(modifier = Modifier.weight(1f, fill = true))
                            }
                        }
                    }
                }
            }
        }

        if (showScrollIndicator) {
            val viewportPx = scrollState.viewportSize.toFloat()
            val totalContentPx = viewportPx + scrollState.maxValue
            val scrollFraction =
                if (scrollState.maxValue > 0) {
                    scrollState.value.toFloat() / scrollState.maxValue
                } else {
                    0f
                }
            val maxScroll = scrollState.maxValue
            val metrics =
                ScrollIndicatorMetrics(
                    viewportPx = viewportPx,
                    totalContentPx = totalContentPx,
                    scrollFraction = scrollFraction,
                    scrollByFraction = { fraction ->
                        if (maxScroll <= 0) return@ScrollIndicatorMetrics
                        val deltaPx = fraction * maxScroll
                        val target = (scrollState.value + deltaPx.toInt()).coerceIn(0, maxScroll)
                        indicatorScope.launch { scrollState.scrollTo(target) }
                    },
                )

            ScrollIndicator(
                metrics = metrics,
                orientation = orientation,
                thickness = indicatorThickness,
                color = resolvedIndicatorColor,
                shape = indicatorShape,
                padding = indicatorPadding,
                position = indicatorPosition,
                autoHide = autoHideIndicator,
                draggable = draggableIndicator,
                activitySignal = scrollState.value,
            )
        }
    }
}

private fun resolveColumnCount(columns: GridColumns, crossAxis: Dp): Int =
    when (columns) {
        is GridColumns.Fixed -> columns.count
        is GridColumns.Adaptive -> {
            val raw = (crossAxis / columns.minSize).toInt()
            raw.coerceAtLeast(1)
        }
    }
