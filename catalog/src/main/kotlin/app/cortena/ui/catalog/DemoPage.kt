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
import framework.cortena.ui.annotation.ExperimentalComponentsApi
import framework.cortena.ui.layout.AppBar

/**
 * Wrapper composable that adds an AppBar above each demo's content.
 *
 * @param title The title shown in the AppBar.
 * @param content The demo content to display below the header.
 */
@OptIn(ExperimentalComponentsApi::class)
@Composable
fun DemoPage(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AppBar(title = title)

        // Demo content
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            content()
        }
    }
}
