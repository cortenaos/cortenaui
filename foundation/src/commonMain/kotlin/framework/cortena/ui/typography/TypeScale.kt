/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.typography

import framework.cortena.ui.size.GoldenRatioSubStep

/**
 * CortenaUI — Type Scale
 *
 * Font size and line height tokens. Values in raw Float (sp equivalent). Framework-agnostic —
 * the Compose layer wraps these as TextUnit.
 *
 * The scale is organized into four semantic categories — Display, Headline, Title, Body —
 * each with three tiers (Small / Medium / Large). Components that need a "label" weight pick
 * the appropriate body or title size and pass `weight = TextWeight.Medium` instead of using a
 * separate label tier.
 *
 * Every size is a power of `⁴√φ` ([GoldenRatioSubStep]) times the [BodyMedium] anchor at 16sp.
 * Tier-to-tier within a category steps by ⁴√φ; category-to-category (Body Large → Title Small)
 * also steps by ⁴√φ, so the full scale lives on a single coherent grid. Two consecutive sub-steps
 * equal √φ; four equal φ — so Body Medium → Title Large → Display Small triples by exact φ
 * jumps, and the whole scale stays mathematically grounded.
 *
 * Line heights use the conventional `~1.4 × fontSize` ratio rather than φ, because line height
 * driven by φ feels visually airy beyond what reads well in body copy.
 */
object TypeScale {

    // Anchor — every other size derives from this.
    private const val Anchor: Float = 14f

    // Powers of ⁴√φ used to walk the scale. Each increment multiplies fontSize by ⁴√φ.
    // Total span: BodySmall (n=-1) … DisplayLarge (n=10), eleven half-steps in all.

    // Body
    const val BodySmall: Float = Anchor / GoldenRatioSubStep
    const val BodyMedium: Float = Anchor
    const val BodyLarge: Float = Anchor * GoldenRatioSubStep

    // Title — starts one sub-step above BodyLarge so categories never overlap.
    const val TitleSmall: Float = BodyLarge * GoldenRatioSubStep
    const val TitleMedium: Float = TitleSmall * GoldenRatioSubStep
    const val TitleLarge: Float = TitleMedium * GoldenRatioSubStep

    // Headline
    const val HeadlineSmall: Float = TitleLarge * GoldenRatioSubStep
    const val HeadlineMedium: Float = HeadlineSmall * GoldenRatioSubStep
    const val HeadlineLarge: Float = HeadlineMedium * GoldenRatioSubStep

    // Display
    const val DisplaySmall: Float = HeadlineLarge * GoldenRatioSubStep
    const val DisplayMedium: Float = DisplaySmall * GoldenRatioSubStep
    const val DisplayLarge: Float = DisplayMedium * GoldenRatioSubStep

    // Line heights — ~1.4 × fontSize, kept manual to avoid the airy feel φ would produce.
    private const val LineHeightFactor: Float = 1.4f

    const val LineHeightBodySmall: Float = BodySmall * LineHeightFactor
    const val LineHeightBodyMedium: Float = BodyMedium * LineHeightFactor
    const val LineHeightBodyLarge: Float = BodyLarge * LineHeightFactor

    const val LineHeightTitleSmall: Float = TitleSmall * LineHeightFactor
    const val LineHeightTitleMedium: Float = TitleMedium * LineHeightFactor
    const val LineHeightTitleLarge: Float = TitleLarge * LineHeightFactor

    const val LineHeightHeadlineSmall: Float = HeadlineSmall * LineHeightFactor
    const val LineHeightHeadlineMedium: Float = HeadlineMedium * LineHeightFactor
    const val LineHeightHeadlineLarge: Float = HeadlineLarge * LineHeightFactor

    const val LineHeightDisplaySmall: Float = DisplaySmall * LineHeightFactor
    const val LineHeightDisplayMedium: Float = DisplayMedium * LineHeightFactor
    const val LineHeightDisplayLarge: Float = DisplayLarge * LineHeightFactor
}
