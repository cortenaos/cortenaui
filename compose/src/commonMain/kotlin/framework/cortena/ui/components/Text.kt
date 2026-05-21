/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.theme.LocalContentColor
import framework.cortena.ui.theme.LocalFontFamily
import framework.cortena.ui.theme.LocalTextRole
import framework.cortena.ui.theme.LocalTextWeight
import framework.cortena.ui.theme.LocalTypography
import framework.cortena.ui.typography.FontStyle
import framework.cortena.ui.typography.TextWeight

enum class TextRole {
    DisplayLarge,
    DisplayMedium,
    DisplaySmall,
    HeadlineLarge,
    HeadlineMedium,
    HeadlineSmall,
    TitleLarge,
    TitleMedium,
    TitleSmall,
    BodyLarge,
    BodyMedium,
    BodySmall,
}

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle? = null,
    role: TextRole? = null,
    weight: TextWeight? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    val colors = LocalColors.current
    val typography = LocalTypography.current
    val fontFamily = LocalFontFamily.current

    // Role / weight resolution: explicit param > scope-level local > Body / Default fallback.
    val resolvedRole = role ?: LocalTextRole.current ?: TextRole.BodyMedium
    val resolvedWeight = weight ?: LocalTextWeight.current ?: TextWeight.Default

    val localContentColor = LocalContentColor.current
    val resolvedColor =
        when {
            color.isSpecified -> color
            localContentColor != null && localContentColor.isSpecified -> localContentColor
            else -> Color(colors.onBackground)
        }

    val roleStyle =
        when (resolvedRole) {
            TextRole.DisplayLarge -> typography.displayLarge
            TextRole.DisplayMedium -> typography.displayMedium
            TextRole.DisplaySmall -> typography.displaySmall
            TextRole.HeadlineLarge -> typography.headlineLarge
            TextRole.HeadlineMedium -> typography.headlineMedium
            TextRole.HeadlineSmall -> typography.headlineSmall
            TextRole.TitleLarge -> typography.titleLarge
            TextRole.TitleMedium -> typography.titleMedium
            TextRole.TitleSmall -> typography.titleSmall
            TextRole.BodyLarge -> typography.bodyLarge
            TextRole.BodyMedium -> typography.bodyMedium
            TextRole.BodySmall -> typography.bodySmall
        }

    // weight = Default falls back to the role's natural weight; any other tier overrides.
    val resolvedFontWeight =
        if (resolvedWeight == TextWeight.Default) roleStyle.fontWeight else resolvedWeight.value

    val resolvedStyle =
        TextStyle(
                fontFamily = fontFamily,
                fontSize = roleStyle.fontSize.sp,
                lineHeight = roleStyle.lineHeight.sp,
                letterSpacing = roleStyle.letterSpacing.sp,
                fontWeight = FontWeight(resolvedFontWeight),
                fontStyle =
                    if (roleStyle.fontStyle == FontStyle.Italic)
                        androidx.compose.ui.text.font.FontStyle.Italic
                    else androidx.compose.ui.text.font.FontStyle.Normal,
            )
            .merge(style)

    BasicText(
        text = text,
        modifier = modifier,
        style = resolvedStyle.copy(color = resolvedColor),
        maxLines = maxLines,
        overflow = overflow,
    )
}
