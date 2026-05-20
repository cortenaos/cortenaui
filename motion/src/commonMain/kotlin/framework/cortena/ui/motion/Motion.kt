/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.motion

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Immutable

/**
 * Aggregate motion language carrier. Holds spring presets, duration tiers, and easings in a
 * single object so components can pull from one source via [LocalMotion].
 *
 * Spring presets:
 *
 * - [snappy]  — tight UI feedback (press, toggle thumb, indicator drag).
 * - [smooth]  — general content shift, panel reveal, item move.
 * - [gentle]  — large overlays, dialog enter/exit, page transition.
 *
 * Durations (ms, `Int` for `tween()` interop):
 *
 * - [fast]   = 150
 * - [medium] = 250
 * - [slow]   = 450
 *
 * Easings:
 *
 * - [standardEasing]   — generic decelerate-into-place curve.
 * - [emphasizedEasing] — stronger deceleration, draws attention.
 * - [linearEasing]     — constant rate, for color/alpha that must change evenly.
 */
@Immutable
public class Motion internal constructor(
    private val springs: SpringPresets,
    private val durations: MotionDurations,
    private val easings: MotionEasings,
) {
    public val snappy: SpringSpec<Float> get() = springs.snappy
    public val smooth: SpringSpec<Float> get() = springs.smooth
    public val gentle: SpringSpec<Float> get() = springs.gentle

    public val fast: Int get() = durations.fast
    public val medium: Int get() = durations.medium
    public val slow: Int get() = durations.slow

    public val standardEasing: Easing get() = easings.standard
    public val emphasizedEasing: Easing get() = easings.emphasized
    public val linearEasing: Easing get() = easings.linear
}

/** Cortena's default motion language. Components fall back to this when no theme provides one. */
public val DefaultMotion: Motion =
    Motion(
        springs = DefaultSpringPresets,
        durations = DefaultMotionDurations,
        easings = DefaultMotionEasings,
    )
