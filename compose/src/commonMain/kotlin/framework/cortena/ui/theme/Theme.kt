/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.font.FontFamily
import framework.cortena.ui.color.DarkPalette
import framework.cortena.ui.color.LightPalette
import framework.cortena.ui.color.Palette
import framework.cortena.ui.motion.DefaultMotion
import framework.cortena.ui.motion.LocalMotion
import framework.cortena.ui.motion.Motion
import framework.cortena.ui.size.SizeToken
import framework.cortena.ui.spacing.Spacing
import framework.cortena.ui.typography.DefaultTypography
import framework.cortena.ui.typography.Typography

@Composable
fun Theme(
    themeMode: ThemeMode = ThemeMode.Auto,
    palette: Palette? = null,
    typography: Typography = DefaultTypography,
    fontFamily: FontFamily? = null,
    sizeToken: SizeToken = SizeToken.Medium,
    motion: Motion = DefaultMotion,
    content: @Composable () -> Unit,
) {
    val isDark =
        when (themeMode) {
            ThemeMode.Light -> false
            ThemeMode.Dark -> true
            ThemeMode.Auto -> isSystemInDarkTheme()
        }

    val resolvedPalette = palette ?: if (isDark) DarkPalette else LightPalette
    val resolvedFontFamily = fontFamily ?: FontFamily.Default

    CompositionLocalProvider(
        LocalIsDark provides isDark,
        LocalColors provides resolvedPalette,
        LocalTypography provides typography,
        LocalFontFamily provides resolvedFontFamily,
        LocalSpacing provides Spacing,
        LocalSizeToken provides sizeToken,
        LocalMotion provides motion,
        content = content,
    )
}
