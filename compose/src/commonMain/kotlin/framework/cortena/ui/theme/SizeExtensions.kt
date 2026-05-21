/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import framework.cortena.ui.size.SizeScale
import framework.cortena.ui.size.SizeToken

// Component height for the given size tier
val SizeToken.componentHeight: Dp
    get() =
        when (this) {
            SizeToken.Small -> SizeScale.HeightSmall.dp
            SizeToken.Medium -> SizeScale.HeightMedium.dp
            SizeToken.Large -> SizeScale.HeightLarge.dp
        }

// Horizontal padding for content inside sized components
val SizeToken.horizontalPadding: Dp
    get() =
        when (this) {
            SizeToken.Small -> SizeScale.HorizontalPaddingSmall.dp
            SizeToken.Medium -> SizeScale.HorizontalPaddingMedium.dp
            SizeToken.Large -> SizeScale.HorizontalPaddingLarge.dp
        }

// Default icon dimension for the given size tier
val SizeToken.iconSize: Dp
    get() =
        when (this) {
            SizeToken.Small -> SizeScale.IconSizeSmall.dp
            SizeToken.Medium -> SizeScale.IconSizeMedium.dp
            SizeToken.Large -> SizeScale.IconSizeLarge.dp
        }

// Spacing between icon and label inside a component
val SizeToken.contentGap: Dp
    get() =
        when (this) {
            SizeToken.Small -> SizeScale.ContentGapSmall.dp
            SizeToken.Medium -> SizeScale.ContentGapMedium.dp
            SizeToken.Large -> SizeScale.ContentGapLarge.dp
        }

// Toggle track width for the given size tier
val SizeToken.toggleTrackWidth: Dp
    get() =
        when (this) {
            SizeToken.Small -> SizeScale.ToggleTrackWidthSmall.dp
            SizeToken.Medium -> SizeScale.ToggleTrackWidthMedium.dp
            SizeToken.Large -> SizeScale.ToggleTrackWidthLarge.dp
        }

// Toggle track height for the given size tier
val SizeToken.toggleTrackHeight: Dp
    get() =
        when (this) {
            SizeToken.Small -> SizeScale.ToggleTrackHeightSmall.dp
            SizeToken.Medium -> SizeScale.ToggleTrackHeightMedium.dp
            SizeToken.Large -> SizeScale.ToggleTrackHeightLarge.dp
        }

// Toggle thumb inset from track edge
val SizeToken.toggleThumbPadding: Dp
    get() =
        when (this) {
            SizeToken.Small -> SizeScale.ToggleThumbPaddingSmall.dp
            SizeToken.Medium -> SizeScale.ToggleThumbPaddingMedium.dp
            SizeToken.Large -> SizeScale.ToggleThumbPaddingLarge.dp
        }

// Toggle sliding capsule width
val SizeToken.toggleThumbWidth: Dp
    get() =
        when (this) {
            SizeToken.Small -> SizeScale.ToggleThumbWidthSmall.dp
            SizeToken.Medium -> SizeScale.ToggleThumbWidthMedium.dp
            SizeToken.Large -> SizeScale.ToggleThumbWidthLarge.dp
        }

// Slider indicator (thumb) width
val SizeToken.sliderIndicatorWidth: Dp
    get() =
        when (this) {
            SizeToken.Small -> SizeScale.SliderIndicatorWidthSmall.dp
            SizeToken.Medium -> SizeScale.SliderIndicatorWidthMedium.dp
            SizeToken.Large -> SizeScale.SliderIndicatorWidthLarge.dp
        }

// Slider track visible height
val SizeToken.sliderTrackHeight: Dp
    get() =
        when (this) {
            SizeToken.Small -> SizeScale.SliderTrackHeightSmall.dp
            SizeToken.Medium -> SizeScale.SliderTrackHeightMedium.dp
            SizeToken.Large -> SizeScale.SliderTrackHeightLarge.dp
        }
