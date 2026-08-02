/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
import framework.cortena.ui.layout.AppBar
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
            fontFamily = FontFamily(
                Font(R.font.generalsans_extralight, FontWeight.ExtraLight),
                Font(R.font.generalsans_extralight_italic, FontWeight.ExtraLight, FontStyle.Italic),
                Font(R.font.generalsans_light, FontWeight.Light),
                Font(R.font.generalsans_light_italic, FontWeight.Light, FontStyle.Italic),
                Font(R.font.generalsans_regular, FontWeight.Normal),
                Font(R.font.generalsans_italic, FontWeight.Normal, FontStyle.Italic),
                Font(R.font.generalsans_medium, FontWeight.Medium),
                Font(R.font.generalsans_medium_italic, FontWeight.Medium, FontStyle.Italic),
                Font(R.font.generalsans_semibold, FontWeight.SemiBold),
                Font(R.font.generalsans_semibold_italic, FontWeight.SemiBold, FontStyle.Italic),
                Font(R.font.generalsans_bold, FontWeight.Bold),
                Font(R.font.generalsans_bold_italic, FontWeight.Bold, FontStyle.Italic)
            )
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

                                    // Dark mode toggle
                                    val isSystemDark = isSystemInDarkTheme()
                                    val isDark =
                                        when (themeMode.value) {
                                            ThemeMode.Light -> false
                                            ThemeMode.Dark -> true
                                            ThemeMode.Auto -> isSystemDark
                                        }

                                    AppBar(
                                        title = {
                                            Text(
                                                "CortenaUI Catalog",
                                                role = TextRole.TitleLarge,
                                                weight = TextWeight.Medium,
                                                color = Color(colors.primary)
                                            )
                                        },
                                        trailing = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text("Dark Mode", role = TextRole.BodyMedium, modifier = Modifier.padding(end = 8.dp))
                                                Toggle(
                                                    checked = isDark,
                                                    onCheckedChange = {
                                                        themeMode.value =
                                                            if (isDark) ThemeMode.Light else ThemeMode.Dark
                                                    },
                                                )
                                            }
                                        }
                                    )

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
