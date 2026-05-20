/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.motion

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * `CompositionLocal` providing the current [Motion] tokens.
 *
 * Components must read motion specs through `LocalMotion.current` rather than constructing
 * `spring(...)` or `tween(...)` calls inline. The default value is [DefaultMotion]; the
 * `Theme {}` entry point in `:compose` is responsible for providing this.
 *
 * Backed by a static composition local — the motion language is global and changes only when
 * the consumer explicitly overrides it (e.g. for accessibility "reduce motion" preferences).
 */
public val LocalMotion = staticCompositionLocalOf { DefaultMotion }
