/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import framework.cortena.icons.PhosphorIcon
import framework.cortena.icons.PhosphorIcons
import framework.cortena.ui.color.ColorToken
import framework.cortena.ui.components.Icon
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.components.Toggle
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.theme.value

@Composable
fun IconDemo() {
    val colors = LocalColors.current
    Text("Icon", color = Color(colors.primary), role = TextRole.TitleMedium)

    var enabled by remember { mutableStateOf(true) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Enable Icon")
        Toggle(checked = enabled, onCheckedChange = { enabled = it })
    }

    Text("Phosphor Icons")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Regular.House),
            contentDescription = "House",
            enabled = enabled
        )
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Regular.Heart),
            contentDescription = "Heart",
            enabled = enabled
        )
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Regular.Gear),
            contentDescription = "Gear",
            enabled = enabled
        )
    }

    Text("Phosphor Weights")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Thin.Star),
            contentDescription = "Thin",
            enabled = enabled
        )
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Light.Star),
            contentDescription = "Light",
            enabled = enabled
        )
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Regular.Star),
            contentDescription = "Regular",
            enabled = enabled
        )
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Bold.Star),
            contentDescription = "Bold",
            enabled = enabled
        )
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Fill.Star),
            contentDescription = "Fill",
            enabled = enabled
        )
    }

    Text("Phosphor with Custom Tint")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Fill.Heart),
            contentDescription = "Red Heart",
            tint = ColorToken.Red500.value(),
            enabled = enabled,
        )
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Fill.Star),
            contentDescription = "Yellow Star",
            tint = ColorToken.Yellow500.value(),
            enabled = enabled,
        )
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Fill.Lightning),
            contentDescription = "Orange Lightning",
            tint = ColorToken.Orange500.value(),
            enabled = enabled,
        )
    }

    Text("Phosphor with Custom Size")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Bold.Rocket),
            contentDescription = "16dp",
            size = 16.dp,
            enabled = enabled,
        )
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Bold.Rocket),
            contentDescription = "24dp",
            size = 24.dp,
            enabled = enabled,
        )
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Bold.Rocket),
            contentDescription = "32dp",
            size = 32.dp,
            enabled = enabled,
        )
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Bold.Rocket),
            contentDescription = "48dp",
            size = 48.dp,
            enabled = enabled,
        )
    }
}
