/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.layout.AppBar
import framework.cortena.ui.typography.TextWeight

/**
 * Wrapper composable that adds an AppBar above each demo's content.
 *
 * @param title The title shown in the AppBar.
 * @param content The demo content to display below the header.
 */
@Composable
fun DemoPage(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AppBar(
            title = { Text(text = title, role = TextRole.TitleMedium, weight = TextWeight.Medium) }
        )

        // Demo content
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            content()
        }
    }
}
