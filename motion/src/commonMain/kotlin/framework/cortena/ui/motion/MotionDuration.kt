/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.motion

import androidx.compose.runtime.Immutable
import framework.cortena.ui.motion.DurationTokens

/**
 * Duration tier in milliseconds, exposed as `Int` so it can be passed directly to
 * [androidx.compose.animation.core.tween]. The raw `Long` values live in [DurationTokens] in
 * `:foundation` and are framework-agnostic; this adapter just narrows them.
 */
@Immutable
public class MotionDurations internal constructor(
    public val fast: Int,
    public val medium: Int,
    public val slow: Int,
)

internal val DefaultMotionDurations: MotionDurations =
    MotionDurations(
        fast = DurationTokens.Fast.toInt(),
        medium = DurationTokens.Medium.toInt(),
        slow = DurationTokens.Slow.toInt(),
    )
