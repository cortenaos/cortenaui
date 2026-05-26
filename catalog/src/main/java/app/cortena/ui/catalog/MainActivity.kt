/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.cortena.ui.catalog.demo.ButtonDemo
import app.cortena.ui.catalog.demo.ColorDemo
import app.cortena.ui.catalog.demo.GridViewDemo
import app.cortena.ui.catalog.demo.LazyGridViewDemo
import app.cortena.ui.catalog.demo.LazyScrollViewDemo
import app.cortena.ui.catalog.demo.ScrollViewDemo
import app.cortena.ui.catalog.demo.SliderDemo
import app.cortena.ui.catalog.demo.ToggleDemo
import app.cortena.ui.catalog.demo.TypographyDemo
import framework.cortena.ui.components.Separator
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.components.Toggle
import framework.cortena.ui.layout.Body
import framework.cortena.ui.layout.ContentView
import framework.cortena.ui.layout.SafeArea
import framework.cortena.ui.layout.ScrollView
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.theme.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeMode = mutableStateOf(ThemeMode.Auto)

        ContentView(
            themeMode = { themeMode.value }
            // Example for use custom FontFamily.
            // fontFamily = FontFamily(Font(R.font.jetbrainsnerdfont_regular))
        ) {
            Body {
                ScrollView {
                    SafeArea {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            val colors = LocalColors.current
                            Text(
                                "Theme",
                                color = Color(colors.primary),
                                role = TextRole.TitleMedium,
                            )
                            val isSystemDark = isSystemInDarkTheme()
                            val isDark =
                                when (themeMode.value) {
                                    ThemeMode.Light -> false
                                    ThemeMode.Dark -> true
                                    ThemeMode.Auto -> isSystemDark
                                }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text("Dark Mode")
                                Toggle(
                                    checked = isDark,
                                    onCheckedChange = {
                                        themeMode.value =
                                            if (isDark) ThemeMode.Light else ThemeMode.Dark
                                    },
                                )
                            }
                            Separator(modifier = Modifier.padding(vertical = 12.dp))
                            TypographyDemo()
                            Separator(modifier = Modifier.padding(vertical = 12.dp))
                            ButtonDemo()
                            Separator(modifier = Modifier.padding(vertical = 12.dp))
                            SliderDemo()
                            Separator(modifier = Modifier.padding(vertical = 12.dp))
                            ToggleDemo()
                            Separator(modifier = Modifier.padding(vertical = 12.dp))
                            ScrollViewDemo()
                            Separator(modifier = Modifier.padding(vertical = 12.dp))
                            LazyScrollViewDemo()
                            Separator(modifier = Modifier.padding(vertical = 12.dp))
                            GridViewDemo()
                            Separator(modifier = Modifier.padding(vertical = 12.dp))
                            LazyGridViewDemo()
                            Separator(modifier = Modifier.padding(vertical = 12.dp))
                            ColorDemo()
                        }
                    }
                }
            }
        }
    }
}
