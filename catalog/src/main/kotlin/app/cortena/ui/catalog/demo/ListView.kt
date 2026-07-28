/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import framework.cortena.ui.components.ListItem
import framework.cortena.ui.components.ListView
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.theme.LocalColors

@Composable
fun ListViewDemo() {
    val colors = LocalColors.current
    Text("ListView", color = Color(colors.primary), role = TextRole.TitleMedium)
    Column {
        ListView(title = "FANCY") {
            val fancyColors =
                listOf(
                    "Red" to Color(0xFFF44336),
                    "Blue" to Color(0xFF2196F3),
                    "Cyan" to Color(0xFF00BCD4),
                    "Mint" to Color(0xFF00E676),
                    "Pink" to Color(0xFFE91E63),
                    "Teal" to Color(0xFF009688),
                    "Green" to Color(0xFF4CAF50),
                    "Brown" to Color(0xFF795548),
                )
            fancyColors.forEach { (name, color) ->
                item {
                    ListItem(
                        title = { Text(name, role = TextRole.BodyMedium) },
                        leading = {
                            Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(color))
                        },
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        ListView(title = "SECONDARY") {
            val secondaryColors =
                listOf(
                    "Label" to Color(0xFF757575),
                    "Fill" to Color(0xFFE0E0E0),
                )
            secondaryColors.forEach { (name, color) ->
                item {
                    ListItem(
                        title = { Text(name, role = TextRole.BodyMedium) },
                        leading = {
                            Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(color))
                        },
                    )
                }
            }
        }
    }
}
