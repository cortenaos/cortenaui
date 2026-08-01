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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import framework.cortena.ui.navigation.LocalNavigator
import framework.cortena.ui.navigation.Navigator
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.theme.ThemeMode
import framework.cortena.ui.typography.TextWeight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeMode = mutableStateOf(ThemeMode.Auto)
        val pageState = mutableStateOf<String?>(null)
        var currentPage by pageState

        val navigator =
            object : Navigator {
                override val canGoBack: Boolean
                    get() = pageState.value != null

                override val previousTitle: String?
                    get() = if (pageState.value != null) "Catalog" else null

                override fun pop() {
                    pageState.value = null
                }
            }

        ContentView(
            themeMode = { themeMode.value },
            // Example for use custom FontFamily.
            // fontFamily = FontFamily(Font(R.font.jetbrainsnerdfont_regular))
        ) {
            CompositionLocalProvider(LocalNavigator provides navigator) {
                Body {
                    // Handle system back press
                    BackHandler(enabled = currentPage != null) { navigator.pop() }

                    ScrollView {
                        SafeArea {
                            if (currentPage == null) {
                                // Main menu
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                ) {
                                    val colors = LocalColors.current

                                    // Header
                                    Text(
                                        "CortenaUI Catalog",
                                        role = TextRole.TitleLarge,
                                        weight = TextWeight.Medium,
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        color = Color(colors.primary)
                                    )

                                    // Dark mode toggle
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

                                    Spacer(modifier = Modifier.height(16.dp))

                                    CatalogMenu(onNavigate = { currentPage = it })
                                }
                            } else {
                                // Demo page
                                DemoPage(title = currentPage!!) {
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
}
