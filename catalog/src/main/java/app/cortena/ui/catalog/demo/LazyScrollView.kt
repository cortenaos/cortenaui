/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.layout.LazyScrollView
import framework.cortena.ui.shape.RoundedShape
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.typography.TextWeight

@Composable
fun LazyScrollViewDemo() {
    val colors = LocalColors.current
    Text("LazyScrollView", color = Color(colors.primary), role = TextRole.TitleMedium)
    Text("Vertical lazy list - only visible items are composed.", role = TextRole.BodySmall)
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(240.dp)
                .background(Color(colors.surfaceVariant), RoundedShape(12.dp))
    ) {
        LazyScrollView(modifier = Modifier.fillMaxWidth()) {
            item {
                Text(
                    "Top of list",
                    role = TextRole.BodyMedium,
                    weight = TextWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                )
            }
            items(50) { index ->
                Text(
                    "Item #$index",
                    role = TextRole.BodyMedium,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                )
            }
            item {
                Text(
                    "End of list",
                    role = TextRole.BodySmall,
                    color = Color(colors.onSurfaceVariant),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                )
            }
        }
    }
    Text("Horizontal lazy row - same DSL, different orientation.", role = TextRole.BodySmall)
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(80.dp)
                .background(Color(colors.surfaceVariant), RoundedShape(12.dp))
    ) {
        LazyScrollView(
            orientation = framework.cortena.ui.geometry.Orientation.Horizontal,
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(30) { index ->
                Box(
                    modifier =
                        Modifier.padding(8.dp)
                            .size(64.dp)
                            .background(Color(colors.primary), RoundedShape(8.dp))
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            "$index",
                            color = Color(colors.onPrimary),
                            role = TextRole.TitleSmall,
                            weight = TextWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}
