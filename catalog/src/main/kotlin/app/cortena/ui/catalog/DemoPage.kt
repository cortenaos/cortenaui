/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import framework.cortena.icons.PhosphorIcon
import framework.cortena.icons.PhosphorIcons
import framework.cortena.ui.components.Icon
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.theme.LocalColors

/**
 * Wrapper composable that adds a back header above each demo's content.
 *
 * @param onBack Callback triggered when the back button / title is tapped.
 * @param content The demo content to display below the header.
 */
@Composable
fun DemoPage(
    onBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    val colors = LocalColors.current

    Column(modifier = Modifier.fillMaxWidth()) {
        // Back header
        Row(
            modifier =
                Modifier.fillMaxWidth().clickable(onClick = onBack).padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                renderer = PhosphorIcon(PhosphorIcons.Bold.CaretLeft),
                contentDescription = "Back",
                tint = Color(colors.primary),
                size = 16.dp,
            )
            Text(
                "Catalog",
                role = TextRole.BodyMedium,
                color = Color(colors.primary),
            )
        }

        // Demo content
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            content()
        }
    }
}
