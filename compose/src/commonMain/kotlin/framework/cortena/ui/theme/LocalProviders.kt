/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import framework.cortena.ui.color.LightPalette
import framework.cortena.ui.color.Palette
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.size.SizeToken
import framework.cortena.ui.spacing.Spacing
import framework.cortena.ui.typography.DefaultTypography
import framework.cortena.ui.typography.TextWeight
import framework.cortena.ui.typography.Typography

val LocalIsDark: ProvidableCompositionLocal<Boolean> = compositionLocalOf { false }
val LocalColors: ProvidableCompositionLocal<Palette> = compositionLocalOf { LightPalette }
val LocalContentColor: ProvidableCompositionLocal<Color?> = compositionLocalOf { null }
val LocalTypography: ProvidableCompositionLocal<Typography> = compositionLocalOf {
    DefaultTypography
}
val LocalFontFamily: ProvidableCompositionLocal<FontFamily> = compositionLocalOf {
    FontFamily.Default
}
val LocalSpacing: ProvidableCompositionLocal<Spacing> = compositionLocalOf { Spacing }
val LocalSizeToken: ProvidableCompositionLocal<SizeToken> = compositionLocalOf { SizeToken.Medium }

/**
 * Implicit text role propagated by sized component scopes (for example [framework.cortena.ui.components.Button])
 * so nested [framework.cortena.ui.components.Text] picks up a role that matches the parent's
 * size tier. `null` means "no scope-level override" — the Text falls back to its own default.
 */
val LocalTextRole: ProvidableCompositionLocal<TextRole?> = compositionLocalOf { null }

/**
 * Implicit text weight propagated by sized component scopes. Same fallback semantics as
 * [LocalTextRole]: `null` means the Text uses its parameter default.
 */
val LocalTextWeight: ProvidableCompositionLocal<TextWeight?> = compositionLocalOf { null }

/**
 * Implicit icon size propagated by sized component scopes so nested
 * [framework.cortena.ui.components.Icon] picks up a size matching the parent's tier. Defaults
 * to 24.dp when no scope is providing one.
 */
val LocalIconSize: ProvidableCompositionLocal<Dp> = compositionLocalOf { 24.dp }
