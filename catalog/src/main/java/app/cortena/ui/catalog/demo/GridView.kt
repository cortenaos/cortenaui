/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.geometry.Orientation
import framework.cortena.ui.layout.GridColumns
import framework.cortena.ui.layout.GridView
import framework.cortena.ui.shape.RoundedShape
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.typography.TextWeight

@Composable
fun GridViewDemo() {
    val colors = LocalColors.current
    Text("GridView", color = Color(colors.primary), role = TextRole.TitleMedium)
    Text(
        "Eager 2D grid - all cells composed upfront. Use for small fixed grids.",
        role = TextRole.BodySmall,
    )
    val labels = (1..12).map { "$it" }
    Text("Fixed (3 columns)", role = TextRole.BodySmall, weight = TextWeight.Medium)
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(200.dp)
                .background(Color(colors.surfaceVariant), RoundedShape(12.dp))
    ) {
        GridView(
            items = labels,
            columns = GridColumns.Fixed(3),
            horizontalSpacing = 8.dp,
            verticalSpacing = 8.dp,
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize(),
        ) { _, label ->
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .height(56.dp)
                        .background(Color(colors.primary), RoundedShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    label,
                    color = Color(colors.onPrimary),
                    role = TextRole.TitleMedium,
                    weight = TextWeight.Bold,
                )
            }
        }
    }
    Text("Adaptive (min 96.dp per cell)", role = TextRole.BodySmall, weight = TextWeight.Medium)
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(200.dp)
                .background(Color(colors.surfaceVariant), RoundedShape(12.dp))
    ) {
        GridView(
            items = labels,
            columns = GridColumns.Adaptive(minSize = 96.dp),
            horizontalSpacing = 8.dp,
            verticalSpacing = 8.dp,
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize(),
        ) { _, label ->
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .height(56.dp)
                        .background(Color(colors.accent), RoundedShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    label,
                    color = Color(colors.onAccent),
                    role = TextRole.TitleMedium,
                    weight = TextWeight.Bold,
                )
            }
        }
    }
    Text(
        "Horizontal (rows scroll, 2 rows of cells)",
        role = TextRole.BodySmall,
        weight = TextWeight.Medium,
    )
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(140.dp)
                .background(Color(colors.surfaceVariant), RoundedShape(12.dp))
    ) {
        GridView(
            items = labels,
            columns = GridColumns.Fixed(2),
            orientation = Orientation.Horizontal,
            horizontalSpacing = 8.dp,
            verticalSpacing = 8.dp,
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize(),
        ) { _, label ->
            Box(
                modifier =
                    Modifier.width(80.dp)
                        .height(56.dp)
                        .background(Color(colors.secondary), RoundedShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    label,
                    color = Color(colors.onSecondary),
                    role = TextRole.TitleSmall,
                    weight = TextWeight.Bold,
                )
            }
        }
    }
}
