/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.size

/**
 * CortenaUI — Size Scale
 *
 * Raw Float constants (dp equivalent) for each [SizeToken] tier. Framework-agnostic — the
 * Compose layer wraps these as Dp.
 *
 * Medium is the anchor for every dimension and matches the existing hardcoded library values,
 * so adopting the size system introduces zero visual regressions at the default tier. Small
 * and Large derive from Medium via a single golden-ratio step ([GoldenRatioStep] = √φ):
 *
 *   Small  = Medium / √φ   (≈ 0.786 × Medium)
 *   Large  = Medium × √φ   (≈ 1.272 × Medium)
 *
 * Two steps span the full golden ratio (Small × φ = Large), so the scale stays
 * mathematically grounded without producing dramatic single-tier jumps.
 *
 * Values are stored at full float precision; no manual rounding. Compose's Dp handles
 * subpixel rendering across density buckets.
 */
object SizeScale {

    // Component height (dp) — used by Button, AppBar touch targets, etc.
    const val HeightMedium: Float = 48f
    const val HeightSmall: Float = HeightMedium / GoldenRatioStep
    const val HeightLarge: Float = HeightMedium * GoldenRatioStep

    // Horizontal padding (dp) — inner content padding for buttons and similar
    const val HorizontalPaddingMedium: Float = 16f
    const val HorizontalPaddingSmall: Float = HorizontalPaddingMedium / GoldenRatioStep
    const val HorizontalPaddingLarge: Float = HorizontalPaddingMedium * GoldenRatioStep

    // Icon size (dp) — default icon dimensions inside sized components
    const val IconSizeMedium: Float = 20f
    const val IconSizeSmall: Float = IconSizeMedium / GoldenRatioStep
    const val IconSizeLarge: Float = IconSizeMedium * GoldenRatioStep

    // Content gap (dp) — spacing between icon and label inside a component
    const val ContentGapMedium: Float = 8f
    const val ContentGapSmall: Float = ContentGapMedium / GoldenRatioStep
    const val ContentGapLarge: Float = ContentGapMedium * GoldenRatioStep

    // Toggle track width (dp)
    const val ToggleTrackWidthMedium: Float = 64f
    const val ToggleTrackWidthSmall: Float = ToggleTrackWidthMedium / GoldenRatioStep
    const val ToggleTrackWidthLarge: Float = ToggleTrackWidthMedium * GoldenRatioStep

    // Toggle track height (dp)
    const val ToggleTrackHeightMedium: Float = 28f
    const val ToggleTrackHeightSmall: Float = ToggleTrackHeightMedium / GoldenRatioStep
    const val ToggleTrackHeightLarge: Float = ToggleTrackHeightMedium * GoldenRatioStep

    // Toggle thumb padding (dp) — inset between track edge and thumb
    const val ToggleThumbPaddingMedium: Float = 2f
    const val ToggleThumbPaddingSmall: Float = ToggleThumbPaddingMedium / GoldenRatioStep
    const val ToggleThumbPaddingLarge: Float = ToggleThumbPaddingMedium * GoldenRatioStep

    // Toggle thumb width (dp) — sliding capsule width, deliberately narrower than the inner track
    const val ToggleThumbWidthMedium: Float = 40f
    const val ToggleThumbWidthSmall: Float = ToggleThumbWidthMedium / GoldenRatioStep
    const val ToggleThumbWidthLarge: Float = ToggleThumbWidthMedium * GoldenRatioStep

    // Slider indicator width (dp)
    const val SliderIndicatorWidthMedium: Float = 48f
    const val SliderIndicatorWidthSmall: Float = SliderIndicatorWidthMedium / GoldenRatioStep
    const val SliderIndicatorWidthLarge: Float = SliderIndicatorWidthMedium * GoldenRatioStep

    // Slider track height (dp) — track/progress bar visible height
    const val SliderTrackHeightMedium: Float = 16f
    const val SliderTrackHeightSmall: Float = SliderTrackHeightMedium / GoldenRatioStep
    const val SliderTrackHeightLarge: Float = SliderTrackHeightMedium * GoldenRatioStep
}
