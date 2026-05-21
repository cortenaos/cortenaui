/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.size

/**
 * The golden ratio φ ≈ 1.6180339887.
 *
 * CortenaUI uses φ as the canonical proportional relationship between distinct compositional
 * groups — for example between a `Body` text role and the next `Title` role. Defined once
 * here so no source file ever has to duplicate the literal `1.618f`.
 */
public const val GoldenRatio: Float = 1.6180339887f

/**
 * One step of the golden ratio √φ ≈ 1.2720196495.
 *
 * Used for tier-to-tier sizing (Small → Medium → Large) inside the size system. Two
 * consecutive [GoldenRatioStep] multiplications equal [GoldenRatio] (`√φ × √φ = φ`),
 * so a Small → Medium → Large progression spans a full golden ratio without any single step
 * feeling like an extreme jump.
 *
 * `SizeScale` uses this for height, padding, icon size, and the rest of the dimension axes.
 */
public const val GoldenRatioStep: Float = 1.2720196495f

/**
 * One typography sub-step ⁴√φ ≈ 1.1283791671.
 *
 * Type tiers within a single category (e.g. `BodySmall` → `BodyMedium` → `BodyLarge`) step
 * by [GoldenRatioSubStep] — softer than [GoldenRatioStep] because font sizes are more
 * sensitive to large jumps. Four consecutive sub-steps span a full golden ratio
 * (`⁴√φ × ⁴√φ × ⁴√φ × ⁴√φ = φ`). Two consecutive sub-steps equal `√φ`, which is also the
 * step between the Large tier of one category and the Small tier of the next. This keeps the
 * full font scale on a single coherent grid: every value is a power of `⁴√φ` times the anchor.
 */
public const val GoldenRatioSubStep: Float = 1.1283791671f
