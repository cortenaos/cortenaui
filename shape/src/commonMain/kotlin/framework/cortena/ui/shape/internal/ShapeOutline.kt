/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.shape.internal

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import framework.cortena.ui.shape.ContinuousCurvature
import framework.cortena.ui.shape.CornerPathReceiver
import framework.cortena.ui.shape.CornerStyle

/**
 * Build a Compose [Outline] for a uniform-radius rectangle, picking the cheapest representation:
 * - Plain [Outline.Rectangle] when the radius is zero.
 * - [Outline.Rounded] for circular corners or when a square fully reaches the capsule limit.
 * - [Outline.Generic] backed by [ContinuousCurvature] for the squircle path.
 */
internal fun shapeOutline(size: Size, radius: Float, style: CornerStyle): Outline {
    val width = size.width
    val height = size.height
    val maxRadius = size.minDimension * 0.5f
    return when {
        radius == 0f -> Outline.Rectangle(Rect(0f, 0f, width, height))

        style == CornerStyle.Circular || (width == height && radius >= maxRadius) -> {
            val cornerRadius = CornerRadius(radius)
            Outline.Rounded(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = width,
                    bottom = height,
                    topLeftCornerRadius = cornerRadius,
                    topRightCornerRadius = cornerRadius,
                    bottomRightCornerRadius = cornerRadius,
                    bottomLeftCornerRadius = cornerRadius,
                )
            )
        }

        else ->
            Outline.Generic(
                Path().also { path ->
                    ContinuousCurvature.emit(
                        width = width,
                        height = height,
                        radius = radius,
                        receiver = ComposePathReceiver(path),
                    )
                }
            )
    }
}

/**
 * Build a Compose [Outline] for a per-corner-radius rectangle.
 *
 * The corner names are absolute (visual) — layout direction has already been resolved by the
 * caller.
 */
internal fun shapeOutline(
    size: Size,
    topLeft: Float,
    topRight: Float,
    bottomRight: Float,
    bottomLeft: Float,
    style: CornerStyle,
): Outline {
    val width = size.width
    val height = size.height
    val maxRadius = size.minDimension * 0.5f
    return when {
        topLeft == 0f && topRight == 0f && bottomRight == 0f && bottomLeft == 0f ->
            Outline.Rectangle(Rect(0f, 0f, width, height))

        style == CornerStyle.Circular ||
            (width == height &&
                topLeft >= maxRadius &&
                topRight >= maxRadius &&
                bottomRight >= maxRadius &&
                bottomLeft >= maxRadius) ->
            Outline.Rounded(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = width,
                    bottom = height,
                    topLeftCornerRadius = CornerRadius(topLeft),
                    topRightCornerRadius = CornerRadius(topRight),
                    bottomRightCornerRadius = CornerRadius(bottomRight),
                    bottomLeftCornerRadius = CornerRadius(bottomLeft),
                )
            )

        else ->
            Outline.Generic(
                Path().also { path ->
                    ContinuousCurvature.emit(
                        width = width,
                        height = height,
                        topLeft = topLeft,
                        topRight = topRight,
                        bottomRight = bottomRight,
                        bottomLeft = bottomLeft,
                        receiver = ComposePathReceiver(path),
                    )
                }
            )
    }
}

/** Adapter that forwards [CornerPathReceiver] commands to a Compose [Path]. */
private class ComposePathReceiver(private val path: Path) : CornerPathReceiver {

    override fun moveTo(x: Float, y: Float) {
        path.moveTo(x, y)
    }

    override fun lineTo(x: Float, y: Float) {
        path.lineTo(x, y)
    }

    override fun cubicTo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float) {
        path.cubicTo(x1, y1, x2, y2, x3, y3)
    }

    override fun close() {
        path.close()
    }
}
