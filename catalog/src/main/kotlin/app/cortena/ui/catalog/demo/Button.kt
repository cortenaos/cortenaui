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
import framework.cortena.ui.components.Button
import framework.cortena.ui.components.ButtonStyle
import framework.cortena.ui.components.ButtonVariant
import framework.cortena.ui.components.Icon
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.components.Toggle
import framework.cortena.ui.size.SizeToken
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.theme.value

@Composable
fun ButtonDemo() {
    val colors = LocalColors.current
    Text("Button", color = Color(colors.primary), role = TextRole.TitleMedium)
    var enable by remember { mutableStateOf(true) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Enable Button")
        Toggle(checked = enable, onCheckedChange = { enable = it })
    }
    Text("Button Default")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(enabled = enable, style = ButtonStyle.Primary) { Text("Primary") }
        Button(enabled = enable, style = ButtonStyle.Secondary) { Text("Secondary") }
        Button(enabled = enable, style = ButtonStyle.Accent) { Text("Accent") }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(enabled = enable, style = ButtonStyle.Ghost) { Text("Ghost") }
        Button(enabled = enable, style = ButtonStyle.Destructive) { Text("Destructive") }
    }
    Text("Button Soft Variant")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(enabled = enable, style = ButtonStyle.Primary, variant = ButtonVariant.Soft) {
            Text("Primary")
        }
        Button(enabled = enable, style = ButtonStyle.Secondary, variant = ButtonVariant.Soft) {
            Text("Secondary")
        }
        Button(enabled = enable, style = ButtonStyle.Accent, variant = ButtonVariant.Soft) {
            Text("Accent")
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(enabled = enable, style = ButtonStyle.Ghost, variant = ButtonVariant.Soft) {
            Text("Ghost")
        }
        Button(enabled = enable, style = ButtonStyle.Destructive, variant = ButtonVariant.Soft) {
            Text("Destructive")
        }
    }
    Text("Other")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(enabled = enable, background = ColorToken.Blue500.value()) {
            Icon(
                renderer = PhosphorIcon(PhosphorIcons.Fill.Heart),
                contentDescription = "Favorite icon",
                tint = ColorToken.Blue50.value(),
            )
            Text("Favorite", color = ColorToken.Blue50.value())
        }
        Button(enabled = enable, background = ColorToken.Green600.value()) { Text("Green") }
        Button(enabled = enable, iconOnly = true, background = ColorToken.Orange500.value()) {
            Icon(
                renderer = PhosphorIcon(PhosphorIcons.Regular.Plus),
                contentDescription = "Add icon",
                tint = Color.White,
            )
        }
        Button(enabled = enable, iconOnly = true, background = ColorToken.Pink500.value()) {
            Icon(
                renderer = PhosphorIcon(PhosphorIcons.Regular.PencilSimple),
                contentDescription = "Edit icon",
                tint = Color.White,
            )
        }
    }
    Text("Sizes")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(enabled = enable, size = SizeToken.Small) { Text("Small") }
        Button(enabled = enable, size = SizeToken.Medium) { Text("Medium") }
        Button(enabled = enable, size = SizeToken.Large) { Text("Large") }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(enabled = enable, iconOnly = true, size = SizeToken.Small) {
            Icon(
                renderer = PhosphorIcon(PhosphorIcons.Regular.Plus),
                contentDescription = "Add icon",
                tint = Color.White,
            )
        }
        Button(enabled = enable, iconOnly = true, size = SizeToken.Medium) {
            Icon(
                renderer = PhosphorIcon(PhosphorIcons.Regular.Plus),
                contentDescription = "Add icon",
                tint = Color.White,
            )
        }
        Button(enabled = enable, iconOnly = true, size = SizeToken.Large) {
            Icon(
                renderer = PhosphorIcon(PhosphorIcons.Regular.Plus),
                contentDescription = "Add icon",
                tint = Color.White,
            )
        }
    }
}
