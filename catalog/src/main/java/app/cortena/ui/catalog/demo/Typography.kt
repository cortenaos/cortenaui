/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import framework.cortena.ui.components.Slider
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.components.Toggle
import framework.cortena.ui.theme.LocalColors

@Composable
fun TypographyDemo() {
    val colors = LocalColors.current
    Text("Typography", color = Color(colors.primary), role = TextRole.TitleMedium)
    var italic by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Italic")
        Toggle(checked = italic, onCheckedChange = { italic = it })
    }
    Text("Display", color = Color(colors.secondary), role = TextRole.TitleMedium)
    Column {
        Text("Display Large", role = TextRole.DisplayLarge)
        Text("Display Medium", role = TextRole.DisplayMedium)
        Text("Display Small", role = TextRole.DisplaySmall)
    }
    Text("Headline", color = Color(colors.secondary), role = TextRole.TitleMedium)
    Column {
        Text("Headline Large", role = TextRole.HeadlineLarge)
        Text("Headline Medium", role = TextRole.HeadlineMedium)
        Text("Headline Small", role = TextRole.HeadlineSmall)
    }
    Text("Title", color = Color(colors.secondary), role = TextRole.TitleMedium)
    Column {
        Text("Title Large", role = TextRole.TitleLarge)
        Text("Title Medium", role = TextRole.TitleMedium)
        Text("Title Small", role = TextRole.TitleSmall)
    }
    Text("Body", color = Color(colors.secondary), role = TextRole.TitleMedium)
    Column {
        Text("Body Large", role = TextRole.BodyLarge)
        Text("Body Medium", role = TextRole.BodyMedium)
        Text("Body Small", role = TextRole.BodySmall)
    }
    Text("Advanced Features", color = Color(colors.secondary), role = TextRole.TitleMedium)
    Column {
        Text("Custom Color (Accent Role Color)", color = Color(colors.accent))
        Text(
            "Merged TextStyle (Underlined + Spacing)",
            style = TextStyle(textDecoration = TextDecoration.Underline, letterSpacing = 2.sp),
            modifier = Modifier.padding(top = 4.dp),
        )
    }
    // Font weight slider — explores the full 100..900 weight axis. Demo uses the raw `style`
    // override; the public API exposes the three named tiers via `TextWeight`.
    var weightValue by remember { mutableFloatStateOf(400f) }
    val weightInt = weightValue.toInt().coerceIn(100, 900)
    val weightLabel = weightLabelFor(weightInt)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Weight")
        Text("$weightInt — $weightLabel", role = TextRole.BodySmall)
    }
    Slider(
        value = { weightValue },
        onValueChange = { weightValue = it },
        valueRange = 100f..900f,
        steps = 7, // 100, 200, 300, 400, 500, 600, 700, 800, 900 → 9 stops, 7 inner steps
    )
    val previewStyle =
        TextStyle(
            fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
            fontWeight = FontWeight(weightInt),
        )
    Column {
        val loremIpsum =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        Text(
            "Ellipsis Overflow: $loremIpsum",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp),
            style = previewStyle,
        )
        Text(
            "Ellipsis Overflow 2 Lines: $loremIpsum",
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp),
            style = previewStyle,
        )
    }
}

// CSS / W3C standard weight names. Used only for the demo label.
private fun weightLabelFor(weight: Int): String =
    when {
        weight < 150 -> "Thin"
        weight < 250 -> "Extra Light"
        weight < 350 -> "Light"
        weight < 450 -> "Regular"
        weight < 550 -> "Medium"
        weight < 650 -> "Semi Bold"
        weight < 750 -> "Bold"
        weight < 850 -> "Extra Bold"
        else -> "Black"
    }
