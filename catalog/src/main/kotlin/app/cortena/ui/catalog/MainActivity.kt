/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cortena.ui.catalog.demo.ButtonDemo
import app.cortena.ui.catalog.demo.ColorDemo
import app.cortena.ui.catalog.demo.GridViewDemo
import app.cortena.ui.catalog.demo.IconDemo
import app.cortena.ui.catalog.demo.LazyGridViewDemo
import app.cortena.ui.catalog.demo.LazyScrollViewDemo
import app.cortena.ui.catalog.demo.ListViewDemo
import app.cortena.ui.catalog.demo.ScrollViewDemo
import app.cortena.ui.catalog.demo.SliderDemo
import app.cortena.ui.catalog.demo.ToggleDemo
import app.cortena.ui.catalog.demo.TypographyDemo
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.components.Toggle
import framework.cortena.ui.layout.Body
import framework.cortena.ui.layout.ContentView
import framework.cortena.ui.layout.SafeArea
import framework.cortena.ui.layout.ScrollView
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.theme.ThemeMode
import framework.cortena.ui.typography.TextWeight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeMode = mutableStateOf(ThemeMode.Auto)
        var currentPage by mutableStateOf<String?>(null)

        ContentView(
            themeMode = { themeMode.value },
            // Example for use custom FontFamily.
            // fontFamily = FontFamily(Font(R.font.jetbrainsnerdfont_regular))
        ) {
            Body {
                // Handle system back press
                BackHandler(enabled = currentPage != null) { currentPage = null }

                ScrollView {
                    SafeArea {
                        if (currentPage == null) {
                            // Main menu
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            ) {
                                // Header
                                Text(
                                    "CortenaUI Catalog",
                                    role = TextRole.TitleLarge,
                                    weight = TextWeight.Medium,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                )

                                // Dark mode toggle
                                val colors = LocalColors.current
                                val isSystemDark = isSystemInDarkTheme()
                                val isDark =
                                    when (themeMode.value) {
                                        ThemeMode.Light -> false
                                        ThemeMode.Dark -> true
                                        ThemeMode.Auto -> isSystemDark
                                    }
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text("Dark Mode", role = TextRole.BodyMedium)
                                    Toggle(
                                        checked = isDark,
                                        onCheckedChange = {
                                            themeMode.value =
                                                if (isDark) ThemeMode.Light else ThemeMode.Dark
                                        },
                                    )
                                }

                                CatalogMenu(onNavigate = { currentPage = it })
                            }
                        } else {
                            // Demo page
                            DemoPage(onBack = { currentPage = null }) {
                                when (currentPage) {
                                    "Button" -> ButtonDemo()
                                    "Icon" -> IconDemo()
                                    "Slider" -> SliderDemo()
                                    "Toggle" -> ToggleDemo()
                                    "ListView" -> ListViewDemo()
                                    "ScrollView" -> ScrollViewDemo()
                                    "LazyScrollView" -> LazyScrollViewDemo()
                                    "GridView" -> GridViewDemo()
                                    "LazyGridView" -> LazyGridViewDemo()
                                    "Typography" -> TypographyDemo()
                                    "Colors" -> ColorDemo()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
