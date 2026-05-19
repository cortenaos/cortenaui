/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.shape

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import framework.cortena.ui.shape.internal.shapeOutline

/**
 * Stadium / pill-shaped rectangle whose corner radius is always half the shorter edge.
 *
 * Used for buttons, toggles, sliders, pill badges, and any element that should remain fully rounded
 * regardless of its size.
 */
@Immutable
public class CapsuleShape(override val style: CornerStyle = CornerStyle.Continuous) :
    ComponentShape {

    override fun corners(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): ComponentShape.Corners {
        val radius = size.minDimension * 0.5f
        return ComponentShape.Corners(
            topLeft = radius,
            topRight = radius,
            bottomRight = radius,
            bottomLeft = radius,
        )
    }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val radius = size.minDimension * 0.5f
        return shapeOutline(size = size, radius = radius, style = style)
    }

    override fun copy(style: CornerStyle): CapsuleShape = CapsuleShape(style = style)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CapsuleShape) return false
        return style == other.style
    }

    override fun hashCode(): Int = style.hashCode()

    override fun toString(): String = "CapsuleShape(style=$style)"
}
