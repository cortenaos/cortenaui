/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.shape

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * Base shape contract used throughout CortenaUI components.
 *
 * Unlike a plain [Shape], a [ComponentShape] exposes its resolved corner radii in pixels via
 * [corners]. This lets renderers that need the radii directly (custom shadows, masks, hit testing)
 * read them without re-implementing layout-direction handling.
 */
@Immutable
public sealed interface ComponentShape : Shape {

    /**
     * Active corner rendering style, or `null` if this shape does not expose a uniform style (for
     * example, a transient lerp shape between two different corner styles).
     */
    public val style: CornerStyle?
        get() = null

    /**
     * Resolve concrete corner radii in pixels for the given [size], [layoutDirection] and
     * [density].
     */
    public fun corners(size: Size, layoutDirection: LayoutDirection, density: Density): Corners

    /** Return a copy of this shape with the given corner [style]. */
    public fun copy(style: CornerStyle): ComponentShape

    /**
     * Resolved corner radii in pixels. Names are absolute (visual) — layout-direction handling has
     * already been applied.
     */
    @Immutable
    public data class Corners(
        val topLeft: Float,
        val topRight: Float,
        val bottomRight: Float,
        val bottomLeft: Float,
    )
}
