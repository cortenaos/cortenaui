/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.geometry.Orientation
import framework.cortena.ui.layout.ScrollView
import framework.cortena.ui.shape.RoundedShape
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.typography.TextWeight

@Composable
fun ScrollViewDemo() {
    val colors = LocalColors.current
    Text("ScrollView", color = Color(colors.primary), role = TextRole.TitleMedium)
    Text(
        "Eager scrollable container - every child is composed upfront. Pulls in CortenaUI's bounce overscroll, auto-hide indicator, and drag-to-scrub.",
        role = TextRole.BodySmall,
    )
    val rows = remember { List(50) { "Item #${it + 1}" } }
    Text(
        "Vertical (two columns side by side)",
        role = TextRole.BodySmall,
        weight = TextWeight.Medium,
    )
    Row(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ScrollView(
            modifier =
                Modifier.weight(1f)
                    .fillMaxSize()
                    .clip(RoundedShape(24.dp))
                    .background(Color(colors.surfaceVariant))
        ) {
            rows.forEach { item ->
                Text(
                    item,
                    role = TextRole.BodyMedium,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                )
            }
        }
        ScrollView(
            modifier =
                Modifier.weight(1f)
                    .fillMaxSize()
                    .clip(RoundedShape(24.dp))
                    .background(Color(colors.surfaceVariant))
        ) {
            rows.forEach { item ->
                Text(
                    item,
                    role = TextRole.BodyMedium,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                )
            }
        }
    }
    Text("Horizontal", role = TextRole.BodySmall, weight = TextWeight.Medium)
    ScrollView(
        orientation = Orientation.Horizontal,
        modifier =
            Modifier.fillMaxWidth()
                .height(80.dp)
                .clip(RoundedShape(24.dp))
                .background(Color(colors.surfaceVariant)),
    ) {
        rows.forEachIndexed { index, _ ->
            Box(
                modifier =
                    Modifier.padding(8.dp)
                        .size(64.dp)
                        .background(Color(colors.primary), RoundedShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "${index + 1}",
                    color = Color(colors.onPrimary),
                    role = TextRole.TitleSmall,
                    weight = TextWeight.Bold,
                )
            }
        }
    }
}
