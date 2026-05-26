/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import framework.cortena.ui.color.ColorToken
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.theme.value

@Composable
fun ColorDemo() {
    val colors = LocalColors.current
    Text("Colors", color = Color(colors.primary), role = TextRole.TitleMedium)
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in
            arrayOf(
                ColorToken.Red50.value(),
                ColorToken.Red100.value(),
                ColorToken.Red200.value(),
                ColorToken.Red300.value(),
                ColorToken.Red400.value(),
                ColorToken.Red500.value(),
                ColorToken.Red600.value(),
                ColorToken.Red700.value(),
                ColorToken.Red800.value(),
                ColorToken.Red900.value(),
                ColorToken.Red950.value(),
            )) {
            Box(modifier = Modifier.height(32.dp).weight(1f).background(i))
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in
            arrayOf(
                ColorToken.Orange50.value(),
                ColorToken.Orange100.value(),
                ColorToken.Orange200.value(),
                ColorToken.Orange300.value(),
                ColorToken.Orange400.value(),
                ColorToken.Orange500.value(),
                ColorToken.Orange600.value(),
                ColorToken.Orange700.value(),
                ColorToken.Orange800.value(),
                ColorToken.Orange900.value(),
                ColorToken.Orange950.value(),
            )) {
            Box(modifier = Modifier.height(32.dp).weight(1f).background(i))
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in
            arrayOf(
                ColorToken.Yellow50.value(),
                ColorToken.Yellow100.value(),
                ColorToken.Yellow200.value(),
                ColorToken.Yellow300.value(),
                ColorToken.Yellow400.value(),
                ColorToken.Yellow500.value(),
                ColorToken.Yellow600.value(),
                ColorToken.Yellow700.value(),
                ColorToken.Yellow800.value(),
                ColorToken.Yellow900.value(),
                ColorToken.Yellow950.value(),
            )) {
            Box(modifier = Modifier.height(32.dp).weight(1f).background(i))
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in
            arrayOf(
                ColorToken.Green50.value(),
                ColorToken.Green100.value(),
                ColorToken.Green200.value(),
                ColorToken.Green300.value(),
                ColorToken.Green400.value(),
                ColorToken.Green500.value(),
                ColorToken.Green600.value(),
                ColorToken.Green700.value(),
                ColorToken.Green800.value(),
                ColorToken.Green900.value(),
                ColorToken.Green950.value(),
            )) {
            Box(modifier = Modifier.height(32.dp).weight(1f).background(i))
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in
            arrayOf(
                ColorToken.Mint50.value(),
                ColorToken.Mint100.value(),
                ColorToken.Mint200.value(),
                ColorToken.Mint300.value(),
                ColorToken.Mint400.value(),
                ColorToken.Mint500.value(),
                ColorToken.Mint600.value(),
                ColorToken.Mint700.value(),
                ColorToken.Mint800.value(),
                ColorToken.Mint900.value(),
                ColorToken.Mint950.value(),
            )) {
            Box(modifier = Modifier.height(32.dp).weight(1f).background(i))
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in
            arrayOf(
                ColorToken.Teal50.value(),
                ColorToken.Teal100.value(),
                ColorToken.Teal200.value(),
                ColorToken.Teal300.value(),
                ColorToken.Teal400.value(),
                ColorToken.Teal500.value(),
                ColorToken.Teal600.value(),
                ColorToken.Teal700.value(),
                ColorToken.Teal800.value(),
                ColorToken.Teal900.value(),
                ColorToken.Teal950.value(),
            )) {
            Box(modifier = Modifier.height(32.dp).weight(1f).background(i))
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in
            arrayOf(
                ColorToken.Cyan50.value(),
                ColorToken.Cyan100.value(),
                ColorToken.Cyan200.value(),
                ColorToken.Cyan300.value(),
                ColorToken.Cyan400.value(),
                ColorToken.Cyan500.value(),
                ColorToken.Cyan600.value(),
                ColorToken.Cyan700.value(),
                ColorToken.Cyan800.value(),
                ColorToken.Cyan900.value(),
                ColorToken.Cyan950.value(),
            )) {
            Box(modifier = Modifier.height(32.dp).weight(1f).background(i))
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in
            arrayOf(
                ColorToken.Blue50.value(),
                ColorToken.Blue100.value(),
                ColorToken.Blue200.value(),
                ColorToken.Blue300.value(),
                ColorToken.Blue400.value(),
                ColorToken.Blue500.value(),
                ColorToken.Blue600.value(),
                ColorToken.Blue700.value(),
                ColorToken.Blue800.value(),
                ColorToken.Blue900.value(),
                ColorToken.Blue950.value(),
            )) {
            Box(modifier = Modifier.height(32.dp).weight(1f).background(i))
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in
            arrayOf(
                ColorToken.Indigo50.value(),
                ColorToken.Indigo100.value(),
                ColorToken.Indigo200.value(),
                ColorToken.Indigo300.value(),
                ColorToken.Indigo400.value(),
                ColorToken.Indigo500.value(),
                ColorToken.Indigo600.value(),
                ColorToken.Indigo700.value(),
                ColorToken.Indigo800.value(),
                ColorToken.Indigo900.value(),
                ColorToken.Indigo950.value(),
            )) {
            Box(modifier = Modifier.height(32.dp).weight(1f).background(i))
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in
            arrayOf(
                ColorToken.Purple50.value(),
                ColorToken.Purple100.value(),
                ColorToken.Purple200.value(),
                ColorToken.Purple300.value(),
                ColorToken.Purple400.value(),
                ColorToken.Purple500.value(),
                ColorToken.Purple600.value(),
                ColorToken.Purple700.value(),
                ColorToken.Purple800.value(),
                ColorToken.Purple900.value(),
                ColorToken.Purple950.value(),
            )) {
            Box(modifier = Modifier.height(32.dp).weight(1f).background(i))
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in
            arrayOf(
                ColorToken.Pink50.value(),
                ColorToken.Pink100.value(),
                ColorToken.Pink200.value(),
                ColorToken.Pink300.value(),
                ColorToken.Pink400.value(),
                ColorToken.Pink500.value(),
                ColorToken.Pink600.value(),
                ColorToken.Pink700.value(),
                ColorToken.Pink800.value(),
                ColorToken.Pink900.value(),
                ColorToken.Pink950.value(),
            )) {
            Box(modifier = Modifier.height(32.dp).weight(1f).background(i))
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in
            arrayOf(
                ColorToken.Brown50.value(),
                ColorToken.Brown100.value(),
                ColorToken.Brown200.value(),
                ColorToken.Brown300.value(),
                ColorToken.Brown400.value(),
                ColorToken.Brown500.value(),
                ColorToken.Brown600.value(),
                ColorToken.Brown700.value(),
                ColorToken.Brown800.value(),
                ColorToken.Brown900.value(),
                ColorToken.Brown950.value(),
            )) {
            Box(modifier = Modifier.height(32.dp).weight(1f).background(i))
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in
            arrayOf(
                ColorToken.Gray50.value(),
                ColorToken.Gray100.value(),
                ColorToken.Gray200.value(),
                ColorToken.Gray300.value(),
                ColorToken.Gray400.value(),
                ColorToken.Gray500.value(),
                ColorToken.Gray600.value(),
                ColorToken.Gray700.value(),
                ColorToken.Gray800.value(),
                ColorToken.Gray900.value(),
                ColorToken.Gray950.value(),
            )) {
            Box(modifier = Modifier.height(32.dp).weight(1f).background(i))
        }
    }
}
