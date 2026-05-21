/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.typography

/**
 * CortenaUI — Text Weight
 *
 * Three opinionated weight tiers exposed alongside [framework.cortena.ui.typography.TextStyle].
 *
 * - [Default]  — fall back to the weight defined by the active text role. Use this for body
 *   copy and paragraph text. Compose components ignore this value and let the role decide.
 * - [Medium]   — semi-bold (500). Use for interactive labels (buttons, tabs, menu items) and
 *   for emphasizing inline runs without escalating to a full title role.
 * - [Bold]     — bold (700). Use sparingly for very strong emphasis.
 *
 * Stored as a raw `Int` weight value matching the W3C CSS scale (100–900). Framework-agnostic;
 * the Compose [framework.cortena.ui.components.Text] composable lifts this into `FontWeight`.
 */
enum class TextWeight(public val value: Int) {
    Default(0),
    Medium(500),
    Bold(700),
}
