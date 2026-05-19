/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.shape

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.util.lerp
import framework.cortena.ui.shape.internal.shapeOutline

/**
 * Linearly interpolate between [start] and [stop] shapes, forcing the result to render with the
 * given [style].
 *
 * Use this overload when the lerp is being driven by a transition between shapes that share a
 * single visual style (for example, animating only the corner radii).
 */
@Stable
public fun lerp(
    start: ComponentShape,
    stop: ComponentShape,
    fraction: Float,
    style: CornerStyle,
): ComponentShape =
    when (fraction) {
        0f -> start
        1f -> stop
        else -> LerpRoundedRectangle(start, stop, fraction, style)
    }

/**
 * Linearly interpolate between [start] and [stop] shapes.
 *
 * If the two shapes have different [CornerStyle]s, the result hard-switches at fraction `0.5`. Pass
 * an explicit `style` argument when smoother behavior is required.
 */
@Stable
public fun lerp(start: ComponentShape, stop: ComponentShape, fraction: Float): ComponentShape =
    when (fraction) {
        0f -> start
        1f -> stop
        else -> {
            val startStyle = start.style
            val stopStyle = stop.style
            val style =
                when {
                    startStyle != null && stopStyle != null ->
                        if (fraction < 0.5f) startStyle else stopStyle

                    startStyle != null -> startStyle
                    stopStyle != null -> stopStyle
                    else -> null
                }
            LerpRoundedRectangle(start, stop, fraction, style)
        }
    }

@Immutable
private data class LerpRoundedRectangle(
    val start: ComponentShape,
    val stop: ComponentShape,
    val fraction: Float,
    override val style: CornerStyle?,
) : ComponentShape {

    override fun corners(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): ComponentShape.Corners {
        val startCorners = start.corners(size, layoutDirection, density)
        val stopCorners = stop.corners(size, layoutDirection, density)
        return if (startCorners == stopCorners) {
            startCorners
        } else {
            val maxRadius = size.minDimension * 0.5f
            ComponentShape.Corners(
                topLeft =
                    lerp(startCorners.topLeft, stopCorners.topLeft, fraction)
                        .fastCoerceIn(0f, maxRadius),
                topRight =
                    lerp(startCorners.topRight, stopCorners.topRight, fraction)
                        .fastCoerceIn(0f, maxRadius),
                bottomRight =
                    lerp(startCorners.bottomRight, stopCorners.bottomRight, fraction)
                        .fastCoerceIn(0f, maxRadius),
                bottomLeft =
                    lerp(startCorners.bottomLeft, stopCorners.bottomLeft, fraction)
                        .fastCoerceIn(0f, maxRadius),
            )
        }
    }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val corners = corners(size, layoutDirection, density)
        if (style == null) {
            return Outline.Rectangle(Rect(0f, 0f, size.width, size.height))
        }
        return if (
            corners.topLeft == corners.topRight &&
                corners.topRight == corners.bottomRight &&
                corners.bottomRight == corners.bottomLeft
        ) {
            shapeOutline(size = size, radius = corners.topLeft, style = style)
        } else {
            shapeOutline(
                size = size,
                topLeft = corners.topLeft,
                topRight = corners.topRight,
                bottomRight = corners.bottomRight,
                bottomLeft = corners.bottomLeft,
                style = style,
            )
        }
    }

    override fun copy(style: CornerStyle): ComponentShape =
        LerpRoundedRectangle(start = start, stop = stop, fraction = fraction, style = style)
}
