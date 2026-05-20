/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.motion

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Immutable

/**
 * Three-tier spring presets for interactive motion.
 *
 * Spring is the default for any motion that originates from user input or follows physical
 * causality (press response, content shift, panel reveal). Use [DurationTokens]-driven tween
 * for deterministic transitions instead — see [Motion] for the easings.
 *
 * Stiffness rationale:
 *
 * - [Snappy]   = StiffnessHigh        — tight UI feedback (press, toggle thumb, indicator drag).
 * - [Smooth]   = StiffnessMediumLow   — general content shift, panel reveal, item move.
 * - [Gentle]   = StiffnessLow         — large overlays, dialog enter/exit, page transition.
 *
 * All three default to no bounce. Components that want a tactile overshoot (e.g. a button
 * press release) should construct a custom `spring()` from these stiffness values plus a
 * higher damping ratio, rather than redefining the stiffness.
 */
@Immutable
public class SpringPresets internal constructor(
    public val snappy: SpringSpec<Float>,
    public val smooth: SpringSpec<Float>,
    public val gentle: SpringSpec<Float>,
)

internal val DefaultSpringPresets: SpringPresets =
    SpringPresets(
        snappy =
            spring(
                stiffness = Spring.StiffnessHigh,
                dampingRatio = Spring.DampingRatioNoBouncy,
            ),
        smooth =
            spring(
                stiffness = Spring.StiffnessMediumLow,
                dampingRatio = Spring.DampingRatioNoBouncy,
            ),
        gentle =
            spring(
                stiffness = Spring.StiffnessLow,
                dampingRatio = Spring.DampingRatioNoBouncy,
            ),
    )
