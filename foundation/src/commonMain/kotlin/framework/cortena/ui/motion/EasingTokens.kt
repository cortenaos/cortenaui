/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.motion

/**
 * Cubic-bezier easing coefficients as raw Float quartets `(a, b, c, d)`.
 *
 * Framework-agnostic: no Compose imports. Compose-side adapters in `:motion` translate these
 * into `CubicBezierEasing` instances.
 *
 * Curves chosen to match the Cortena motion language:
 *
 * - [Standard] — generic decelerate-into-place curve. Use for most fades and transitions.
 * - [Emphasized] — stronger deceleration. Use when the arrival should draw attention.
 * - [Linear] — constant rate. Use for color/alpha that must change evenly over time.
 */
public object EasingTokens {
    public val Standard: FloatArray = floatArrayOf(0.4f, 0.0f, 0.2f, 1.0f)
    public val Emphasized: FloatArray = floatArrayOf(0.2f, 0.0f, 0.0f, 1.0f)
    public val Linear: FloatArray = floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f)
}
