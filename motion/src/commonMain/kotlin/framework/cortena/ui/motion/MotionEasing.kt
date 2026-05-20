/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.motion

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.runtime.Immutable
import framework.cortena.ui.motion.EasingTokens

/**
 * Compose-aware easing presets, derived from the framework-agnostic [EasingTokens] in
 * `:foundation`. Use these inside `tween()` calls for deterministic transitions where a
 * spring would feel too organic — color crossfade, alpha fade, indicator hide.
 */
@Immutable
public class MotionEasings internal constructor(
    public val standard: Easing,
    public val emphasized: Easing,
    public val linear: Easing,
)

internal val DefaultMotionEasings: MotionEasings =
    MotionEasings(
        standard = EasingTokens.Standard.toEasing(),
        emphasized = EasingTokens.Emphasized.toEasing(),
        linear = LinearEasing,
    )

private fun FloatArray.toEasing(): Easing {
    require(size == 4) { "Cubic bezier easing token must have exactly 4 coefficients." }
    return CubicBezierEasing(this[0], this[1], this[2], this[3])
}
