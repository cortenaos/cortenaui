/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.shape

import framework.cortena.ui.shape.internal.CornerBuilder

/**
 * Pure-Kotlin geometry builders for continuous-curvature ("squircle") rounded rectangles.
 *
 * These functions intentionally take no graphics framework types. They emit drawing commands to a
 * [CornerPathReceiver], allowing the same geometry to be rendered into Compose `Path`,
 * `android.graphics.Path`, Skia, SVG, or any other path API.
 *
 * Coordinate space: the rectangle starts at (0, 0) and extends to (`width`, `height`). All
 * coordinates emitted to the receiver are in pixels.
 *
 * Use these from non-Compose consumers (e.g. system UI surfaces, Android View custom drawing) when
 * you need the same corner mathematics that powers the Compose `ContinuousCurvature` shapes in the
 * `:shape` module.
 */
public object ContinuousCurvature {

    /**
     * Emit a continuous-curvature rounded rectangle with the same [radius] applied to all four
     * corners.
     *
     * The path is emitted as one [CornerPathReceiver.moveTo], a sequence of cubic bezier and
     * straight segments, and one final [CornerPathReceiver.close].
     *
     * @param width Rectangle width, in pixels. Must be non-negative.
     * @param height Rectangle height, in pixels. Must be non-negative.
     * @param radius Corner radius, in pixels. The effective radius is clamped to half the shorter
     *   edge.
     * @param receiver Sink that receives drawing commands.
     */
    public fun emit(width: Float, height: Float, radius: Float, receiver: CornerPathReceiver) {
        val cornerBuilder = CornerBuilder.Default
        val w = width.toDouble()
        val h = height.toDouble()
        val r = radius.toDouble()
        if (r <= 0.0) {
            emitRectangle(width, height, receiver)
            return
        }

        val tW = ((width * 0.5 - r) / r).coerceIn(0.0, 1.0)
        val tH = ((height * 0.5 - r) / r).coerceIn(0.0, 1.0)
        val p = cornerBuilder.getCornerBezierPoints(tW, tH)
        if (p.size < BEZIER_POINT_PAIRS * 2) return

        // Top-right corner.
        var x = w - r
        var y = 0.0
        receiver.moveTo((x + p[0] * r).toFloat(), (y + p[1] * r).toFloat())
        receiver.cubicTo(
            (x + p[2] * r).toFloat(),
            (y + p[3] * r).toFloat(),
            (x + p[4] * r).toFloat(),
            (y + p[5] * r).toFloat(),
            (x + p[6] * r).toFloat(),
            (y + p[7] * r).toFloat(),
        )
        receiver.cubicTo(
            (x + p[8] * r).toFloat(),
            (y + p[9] * r).toFloat(),
            (x + p[10] * r).toFloat(),
            (y + p[11] * r).toFloat(),
            (x + p[12] * r).toFloat(),
            (y + p[13] * r).toFloat(),
        )
        receiver.cubicTo(
            (x + p[14] * r).toFloat(),
            (y + p[15] * r).toFloat(),
            (x + p[16] * r).toFloat(),
            (y + p[17] * r).toFloat(),
            (x + p[18] * r).toFloat(),
            (y + p[19] * r).toFloat(),
        )

        // Bottom-right corner.
        x = w - r
        y = h
        receiver.lineTo((x + p[18] * r).toFloat(), (y - p[19] * r).toFloat())
        receiver.cubicTo(
            (x + p[16] * r).toFloat(),
            (y - p[17] * r).toFloat(),
            (x + p[14] * r).toFloat(),
            (y - p[15] * r).toFloat(),
            (x + p[12] * r).toFloat(),
            (y - p[13] * r).toFloat(),
        )
        receiver.cubicTo(
            (x + p[10] * r).toFloat(),
            (y - p[11] * r).toFloat(),
            (x + p[8] * r).toFloat(),
            (y - p[9] * r).toFloat(),
            (x + p[6] * r).toFloat(),
            (y - p[7] * r).toFloat(),
        )
        receiver.cubicTo(
            (x + p[4] * r).toFloat(),
            (y - p[5] * r).toFloat(),
            (x + p[2] * r).toFloat(),
            (y - p[3] * r).toFloat(),
            (x + p[0] * r).toFloat(),
            (y - p[1] * r).toFloat(),
        )

        // Bottom-left corner.
        x = r
        y = h
        receiver.lineTo((x - p[0] * r).toFloat(), (y - p[1] * r).toFloat())
        receiver.cubicTo(
            (x - p[2] * r).toFloat(),
            (y - p[3] * r).toFloat(),
            (x - p[4] * r).toFloat(),
            (y - p[5] * r).toFloat(),
            (x - p[6] * r).toFloat(),
            (y - p[7] * r).toFloat(),
        )
        receiver.cubicTo(
            (x - p[8] * r).toFloat(),
            (y - p[9] * r).toFloat(),
            (x - p[10] * r).toFloat(),
            (y - p[11] * r).toFloat(),
            (x - p[12] * r).toFloat(),
            (y - p[13] * r).toFloat(),
        )
        receiver.cubicTo(
            (x - p[14] * r).toFloat(),
            (y - p[15] * r).toFloat(),
            (x - p[16] * r).toFloat(),
            (y - p[17] * r).toFloat(),
            (x - p[18] * r).toFloat(),
            (y - p[19] * r).toFloat(),
        )

        // Top-left corner.
        x = r
        y = 0.0
        receiver.lineTo((x - p[18] * r).toFloat(), (y + p[19] * r).toFloat())
        receiver.cubicTo(
            (x - p[16] * r).toFloat(),
            (y + p[17] * r).toFloat(),
            (x - p[14] * r).toFloat(),
            (y + p[15] * r).toFloat(),
            (x - p[12] * r).toFloat(),
            (y + p[13] * r).toFloat(),
        )
        receiver.cubicTo(
            (x - p[10] * r).toFloat(),
            (y + p[11] * r).toFloat(),
            (x - p[8] * r).toFloat(),
            (y + p[9] * r).toFloat(),
            (x - p[6] * r).toFloat(),
            (y + p[7] * r).toFloat(),
        )
        receiver.cubicTo(
            (x - p[4] * r).toFloat(),
            (y + p[5] * r).toFloat(),
            (x - p[2] * r).toFloat(),
            (y + p[3] * r).toFloat(),
            (x - p[0] * r).toFloat(),
            (y + p[1] * r).toFloat(),
        )

        receiver.close()
    }

    /**
     * Emit a continuous-curvature rounded rectangle with independent radii per corner.
     *
     * Corner names are in absolute (visual) order, not start/end. Callers are responsible for
     * resolving layout direction before calling this function.
     *
     * Each corner radius is independently clamped to the half of the shorter edge. Negative radii
     * are treated as zero. If all four radii are zero, a plain rectangle is emitted.
     *
     * @param width Rectangle width, in pixels. Must be non-negative.
     * @param height Rectangle height, in pixels. Must be non-negative.
     * @param topLeft Top-left corner radius, in pixels.
     * @param topRight Top-right corner radius, in pixels.
     * @param bottomRight Bottom-right corner radius, in pixels.
     * @param bottomLeft Bottom-left corner radius, in pixels.
     * @param receiver Sink that receives drawing commands.
     */
    public fun emit(
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float,
        receiver: CornerPathReceiver,
    ) {
        if (topLeft <= 0f && topRight <= 0f && bottomRight <= 0f && bottomLeft <= 0f) {
            emitRectangle(width, height, receiver)
            return
        }

        val cornerBuilder = CornerBuilder.Default
        val w = width.toDouble()
        val h = height.toDouble()

        // Top-right corner.
        var r = topRight.toDouble()
        var tW = ((width * 0.5 - r) / r).coerceIn(0.0, 1.0)
        var tH = ((height * 0.5 - r) / r).coerceIn(0.0, 1.0)
        var p = cornerBuilder.getCornerBezierPoints(tW, tH)
        if (p.size < BEZIER_POINT_PAIRS * 2) return

        var x = w - r
        var y = 0.0
        receiver.moveTo((x + p[0] * r).toFloat(), (y + p[1] * r).toFloat())
        receiver.cubicTo(
            (x + p[2] * r).toFloat(),
            (y + p[3] * r).toFloat(),
            (x + p[4] * r).toFloat(),
            (y + p[5] * r).toFloat(),
            (x + p[6] * r).toFloat(),
            (y + p[7] * r).toFloat(),
        )
        receiver.cubicTo(
            (x + p[8] * r).toFloat(),
            (y + p[9] * r).toFloat(),
            (x + p[10] * r).toFloat(),
            (y + p[11] * r).toFloat(),
            (x + p[12] * r).toFloat(),
            (y + p[13] * r).toFloat(),
        )
        receiver.cubicTo(
            (x + p[14] * r).toFloat(),
            (y + p[15] * r).toFloat(),
            (x + p[16] * r).toFloat(),
            (y + p[17] * r).toFloat(),
            (x + p[18] * r).toFloat(),
            (y + p[19] * r).toFloat(),
        )

        // Bottom-right corner.
        r = bottomRight.toDouble()
        tW = ((width * 0.5 - r) / r).coerceIn(0.0, 1.0)
        tH = ((height * 0.5 - r) / r).coerceIn(0.0, 1.0)
        p = cornerBuilder.getCornerBezierPoints(tW, tH)
        if (p.size < BEZIER_POINT_PAIRS * 2) return

        x = w - r
        y = h
        receiver.lineTo((x + p[18] * r).toFloat(), (y - p[19] * r).toFloat())
        receiver.cubicTo(
            (x + p[16] * r).toFloat(),
            (y - p[17] * r).toFloat(),
            (x + p[14] * r).toFloat(),
            (y - p[15] * r).toFloat(),
            (x + p[12] * r).toFloat(),
            (y - p[13] * r).toFloat(),
        )
        receiver.cubicTo(
            (x + p[10] * r).toFloat(),
            (y - p[11] * r).toFloat(),
            (x + p[8] * r).toFloat(),
            (y - p[9] * r).toFloat(),
            (x + p[6] * r).toFloat(),
            (y - p[7] * r).toFloat(),
        )
        receiver.cubicTo(
            (x + p[4] * r).toFloat(),
            (y - p[5] * r).toFloat(),
            (x + p[2] * r).toFloat(),
            (y - p[3] * r).toFloat(),
            (x + p[0] * r).toFloat(),
            (y - p[1] * r).toFloat(),
        )

        // Bottom-left corner.
        r = bottomLeft.toDouble()
        tW = ((width * 0.5 - r) / r).coerceIn(0.0, 1.0)
        tH = ((height * 0.5 - r) / r).coerceIn(0.0, 1.0)
        p = cornerBuilder.getCornerBezierPoints(tW, tH)
        if (p.size < BEZIER_POINT_PAIRS * 2) return

        x = r
        y = h
        receiver.lineTo((x - p[0] * r).toFloat(), (y - p[1] * r).toFloat())
        receiver.cubicTo(
            (x - p[2] * r).toFloat(),
            (y - p[3] * r).toFloat(),
            (x - p[4] * r).toFloat(),
            (y - p[5] * r).toFloat(),
            (x - p[6] * r).toFloat(),
            (y - p[7] * r).toFloat(),
        )
        receiver.cubicTo(
            (x - p[8] * r).toFloat(),
            (y - p[9] * r).toFloat(),
            (x - p[10] * r).toFloat(),
            (y - p[11] * r).toFloat(),
            (x - p[12] * r).toFloat(),
            (y - p[13] * r).toFloat(),
        )
        receiver.cubicTo(
            (x - p[14] * r).toFloat(),
            (y - p[15] * r).toFloat(),
            (x - p[16] * r).toFloat(),
            (y - p[17] * r).toFloat(),
            (x - p[18] * r).toFloat(),
            (y - p[19] * r).toFloat(),
        )

        // Top-left corner.
        r = topLeft.toDouble()
        tW = ((width * 0.5 - r) / r).coerceIn(0.0, 1.0)
        tH = ((height * 0.5 - r) / r).coerceIn(0.0, 1.0)
        p = cornerBuilder.getCornerBezierPoints(tW, tH)
        if (p.size < BEZIER_POINT_PAIRS * 2) return

        x = r
        y = 0.0
        receiver.lineTo((x - p[18] * r).toFloat(), (y + p[19] * r).toFloat())
        receiver.cubicTo(
            (x - p[16] * r).toFloat(),
            (y + p[17] * r).toFloat(),
            (x - p[14] * r).toFloat(),
            (y + p[15] * r).toFloat(),
            (x - p[12] * r).toFloat(),
            (y + p[13] * r).toFloat(),
        )
        receiver.cubicTo(
            (x - p[10] * r).toFloat(),
            (y + p[11] * r).toFloat(),
            (x - p[8] * r).toFloat(),
            (y + p[9] * r).toFloat(),
            (x - p[6] * r).toFloat(),
            (y + p[7] * r).toFloat(),
        )
        receiver.cubicTo(
            (x - p[4] * r).toFloat(),
            (y + p[5] * r).toFloat(),
            (x - p[2] * r).toFloat(),
            (y + p[3] * r).toFloat(),
            (x - p[0] * r).toFloat(),
            (y + p[1] * r).toFloat(),
        )

        receiver.close()
    }

    private fun emitRectangle(width: Float, height: Float, receiver: CornerPathReceiver) {
        receiver.moveTo(0f, 0f)
        receiver.lineTo(width, 0f)
        receiver.lineTo(width, height)
        receiver.lineTo(0f, height)
        receiver.close()
    }

    // Each corner contributes 10 (x, y) pairs to the bezier point array.
    private const val BEZIER_POINT_PAIRS = 10
}
