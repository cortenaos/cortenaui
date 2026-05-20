/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.motion

/**
 * Raw duration tokens in milliseconds.
 *
 * Framework-agnostic: pure Long values, no Compose / animation library imports. Compose-side
 * adapters in `:motion` translate these into `tween()` durations.
 *
 * Tier rationale:
 *
 * - [Fast] — tap acknowledgement, small fades, color shifts. Should feel instant.
 * - [Medium] — standard fades, indicator hide, content crossfade. The default tier.
 * - [Slow] — emphasis transitions, dialog scrim, page transition. Used sparingly.
 */
public object DurationTokens {
    public const val Fast: Long = 150L
    public const val Medium: Long = 250L
    public const val Slow: Long = 450L
}
