/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.shape

/**
 * Sink that receives path commands emitted by the shape geometry builders in this module.
 *
 * The receiver pattern decouples the pure-Kotlin shape mathematics from any concrete graphics
 * framework. Consumers implement this interface to forward commands to their target path type:
 * - Compose consumers wrap `androidx.compose.ui.graphics.Path`.
 * - Android View / Canvas consumers wrap `android.graphics.Path`.
 * - SVG, Skia, or system-level renderers wrap their respective path APIs.
 *
 * All coordinates are in pixels in the destination coordinate space. The builders never call
 * `close()` more than once per call, and emit commands in the order: one [moveTo] followed by a
 * sequence of [lineTo] / [cubicTo] calls and a final [close].
 */
public interface CornerPathReceiver {

    /** Move the pen to the given point without drawing. Always called once at the start. */
    public fun moveTo(x: Float, y: Float)

    /** Draw a straight segment from the current pen position to the given point. */
    public fun lineTo(x: Float, y: Float)

    /**
     * Draw a cubic Bezier segment from the current pen position to (x3, y3) with control points
     * (x1, y1) and (x2, y2).
     */
    public fun cubicTo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float)

    /** Close the current sub-path with a straight segment back to its start point. */
    public fun close()
}
