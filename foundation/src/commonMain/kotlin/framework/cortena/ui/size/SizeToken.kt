/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.size

/**
 * CortenaUI — Size Token
 *
 * Three component size tiers used throughout CortenaUI. Components read the active [SizeToken]
 * from the theme to determine their height, padding, icon size, and other dimensions.
 *
 * The three tiers are derived from a single anchor: [Medium]. [Small] is `Medium / φ` and
 * [Large] is `Medium × φ`, where φ is the golden ratio (≈ 1.618). Anchoring at Medium keeps
 * existing components visually stable while giving Small and Large mathematically grounded
 * proportions.
 *
 * [Medium] is the default tier and matches all hardcoded component dimensions in the library,
 * ensuring zero visual regressions for current consumers.
 */
enum class SizeToken {
    Small,
    Medium,
    Large,
}
