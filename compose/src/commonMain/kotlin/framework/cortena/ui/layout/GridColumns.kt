/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.layout

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * How a grid lays out its columns (or rows for horizontal grids).
 * - [Fixed] — exact column count. The grid divides the cross-axis into [count] equal cells.
 * - [Adaptive] — grow as many columns as fit while keeping each at least [minSize] wide. Useful for
 *   responsive grids that adapt to phone / tablet / desktop widths automatically.
 */
@Immutable
sealed class GridColumns {

    /** A grid with exactly [count] columns. Cells share the cross-axis evenly. */
    @Immutable
    data class Fixed(val count: Int) : GridColumns() {
        init {
            require(count > 0) { "GridColumns.Fixed count must be > 0, was $count." }
        }
    }

    /**
     * A responsive grid that fits as many columns as possible while keeping every cell at least
     * [minSize] wide on the cross axis. The grid recomputes column count whenever the available
     * width changes.
     */
    @Immutable
    data class Adaptive(val minSize: Dp) : GridColumns() {
        init {
            require(minSize.value > 0f) {
                "GridColumns.Adaptive minSize must be > 0, was $minSize."
            }
        }
    }
}
