/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import framework.cortena.ui.components.Slider
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.components.Toggle
import framework.cortena.ui.size.SizeToken
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.typography.TextWeight

@Composable
fun SliderDemo() {
    val colors = LocalColors.current
    Text("Slider", color = Color(colors.primary), role = TextRole.TitleMedium)
    var enable by remember { mutableStateOf(true) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Enable Slider")
        Toggle(checked = enable, onCheckedChange = { enable = it })
    }
    var sliderValue by remember { mutableFloatStateOf(0f) }
    Text("Slider")
    Column {
        Text("value int: ${sliderValue.toInt()}")
        Text("value float: $sliderValue")
    }
    Slider(
        value = { sliderValue },
        onValueChange = { sliderValue = it },
        valueRange = -4f..4f,
        enabled = enable,
    )
    var sliderDiscreteValue by remember { mutableFloatStateOf(0f) }
    Text("Slider Discrete")
    Text("value snapped: $sliderDiscreteValue")
    Slider(
        value = { sliderDiscreteValue },
        onValueChange = { sliderDiscreteValue = it },
        valueRange = -2f..2f,
        steps = 3, // Creates 4 intervals between -2 and 2, meaning a step size of 1f.
        enabled = enable,
    )
    Text("Sizes")
    val sizeLabels = listOf("S", "M", "L")
    SizeToken.entries.forEachIndexed { index, sizeToken ->
        var sizeValue by remember { mutableFloatStateOf(0.5f) }
        Text(sizeLabels[index], role = TextRole.BodySmall, weight = TextWeight.Medium)
        Slider(
            value = { sizeValue },
            onValueChange = { sizeValue = it },
            valueRange = 0f..1f,
            size = sizeToken,
            enabled = enable,
        )
    }
}
