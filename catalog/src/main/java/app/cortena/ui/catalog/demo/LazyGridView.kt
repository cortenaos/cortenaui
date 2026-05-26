/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.geometry.Orientation
import framework.cortena.ui.layout.GridColumns
import framework.cortena.ui.layout.LazyGridView
import framework.cortena.ui.shape.RoundedShape
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.typography.TextWeight

@Composable
fun LazyGridViewDemo() {
    val colors = LocalColors.current
    Text("LazyGridView", color = Color(colors.primary), role = TextRole.TitleMedium)
    Text(
        "Lazy 2D grid - only cells in the viewport are composed. Use for large grids.",
        role = TextRole.BodySmall,
    )
    Text(
        "Adaptive (min 80.dp per cell), 200 items",
        role = TextRole.BodySmall,
        weight = TextWeight.Medium,
    )
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(280.dp)
                .background(Color(colors.surfaceVariant), RoundedShape(12.dp))
    ) {
        LazyGridView(
            columns = GridColumns.Adaptive(minSize = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items((1..200).toList()) { value ->
                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .height(72.dp)
                            .background(Color(colors.primary), RoundedShape(10.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "$value",
                        color = Color(colors.onPrimary),
                        role = TextRole.TitleSmall,
                        weight = TextWeight.Bold,
                    )
                }
            }
        }
    }
    Text(
        "Horizontal lazy grid (Fixed 3 rows, 201 items)",
        role = TextRole.BodySmall,
        weight = TextWeight.Medium,
    )
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(220.dp)
                .background(Color(colors.surfaceVariant), RoundedShape(12.dp))
    ) {
        LazyGridView(
            columns = GridColumns.Fixed(3),
            orientation = Orientation.Horizontal,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items((1..201).toList()) { value ->
                Box(
                    modifier =
                        Modifier.width(56.dp)
                            .height(56.dp)
                            .background(Color(colors.accent), RoundedShape(10.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "$value",
                        color = Color(colors.onAccent),
                        role = TextRole.TitleSmall,
                        weight = TextWeight.Bold,
                    )
                }
            }
        }
    }
}
