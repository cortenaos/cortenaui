/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.shape

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.util.fastCoerceIn
import framework.cortena.ui.shape.internal.shapeOutline

/**
 * Return a uniform [RoundedShape] using this shape's [CornerStyle] (or [CornerStyle.Continuous]).
 */
@Stable
public fun ComponentShape.copy(
    cornerRadius: Dp,
    style: CornerStyle = this.style ?: CornerStyle.Continuous,
): RoundedShape = RoundedShape(cornerRadius = cornerRadius, style = style)

/**
 * Return a per-corner [UnevenShape] using this shape's [CornerStyle] (or [CornerStyle.Continuous]).
 */
@Stable
public fun ComponentShape.copy(
    cornerRadii: CornerRadii,
    style: CornerStyle = this.style ?: CornerStyle.Continuous,
): UnevenShape = UnevenShape(cornerRadii = cornerRadii, style = style)

/**
 * Override individual corner radii on top of an existing shape.
 *
 * Unspecified [Dp] arguments inherit the corresponding corner radius from this shape, so callers
 * can selectively change a subset of corners without re-stating the others. The result preserves
 * layout-direction handling because radii are interpreted in start/end semantics.
 */
@Stable
public fun ComponentShape.copy(
    topStart: Dp = Dp.Unspecified,
    topEnd: Dp = Dp.Unspecified,
    bottomEnd: Dp = Dp.Unspecified,
    bottomStart: Dp = Dp.Unspecified,
    style: CornerStyle = this.style ?: CornerStyle.Continuous,
): ComponentShape =
    CopyRoundedRectangle(
        shape = this,
        topStart = topStart,
        topEnd = topEnd,
        bottomEnd = bottomEnd,
        bottomStart = bottomStart,
        style = style,
    )

@Immutable
private data class CopyRoundedRectangle(
    val shape: ComponentShape,
    val topStart: Dp,
    val topEnd: Dp,
    val bottomEnd: Dp,
    val bottomStart: Dp,
    override val style: CornerStyle,
) : ComponentShape {

    override fun corners(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): ComponentShape.Corners {
        val corners = shape.corners(size, layoutDirection, density)
        val maxRadius = size.minDimension * 0.5f

        var topLeft = corners.topLeft
        var topRight = corners.topRight
        var bottomRight = corners.bottomRight
        var bottomLeft = corners.bottomLeft
        with(density) {
            when (layoutDirection) {
                LayoutDirection.Ltr -> {
                    if (topStart.isSpecified) topLeft = topStart.toPx().fastCoerceIn(0f, maxRadius)
                    if (topEnd.isSpecified) topRight = topEnd.toPx().fastCoerceIn(0f, maxRadius)
                    if (bottomEnd.isSpecified)
                        bottomRight = bottomEnd.toPx().fastCoerceIn(0f, maxRadius)
                    if (bottomStart.isSpecified)
                        bottomLeft = bottomStart.toPx().fastCoerceIn(0f, maxRadius)
                }

                LayoutDirection.Rtl -> {
                    if (topStart.isSpecified) topRight = topStart.toPx().fastCoerceIn(0f, maxRadius)
                    if (topEnd.isSpecified) topLeft = topEnd.toPx().fastCoerceIn(0f, maxRadius)
                    if (bottomEnd.isSpecified)
                        bottomLeft = bottomEnd.toPx().fastCoerceIn(0f, maxRadius)
                    if (bottomStart.isSpecified)
                        bottomRight = bottomStart.toPx().fastCoerceIn(0f, maxRadius)
                }
            }
        }

        return ComponentShape.Corners(
            topLeft = topLeft,
            topRight = topRight,
            bottomRight = bottomRight,
            bottomLeft = bottomLeft,
        )
    }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val corners = corners(size, layoutDirection, density)
        return shapeOutline(
            size = size,
            topLeft = corners.topLeft,
            topRight = corners.topRight,
            bottomRight = corners.bottomRight,
            bottomLeft = corners.bottomLeft,
            style = style,
        )
    }

    override fun copy(style: CornerStyle): ComponentShape =
        CopyRoundedRectangle(
            shape = shape,
            topStart = topStart,
            topEnd = topEnd,
            bottomEnd = bottomEnd,
            bottomStart = bottomStart,
            style = style,
        )
}
