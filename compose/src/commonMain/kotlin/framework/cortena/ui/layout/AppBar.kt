/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment as UiAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import framework.cortena.ui.annotation.ExperimentalComponentsApi
import framework.cortena.ui.components.Button
import framework.cortena.ui.components.ButtonStyle
import framework.cortena.ui.components.ButtonVariant
import framework.cortena.ui.components.Icon
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.navigation.LocalNavigator
import framework.cortena.ui.size.SizeToken
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.typography.TextWeight

internal val APP_BAR_HEIGHT_DEFAULT = 56.dp

val LocalAppBarSlot =
    compositionLocalOf<MutableState<(@Composable () -> Unit)?>> {
        error("LocalAppBarSlot not provided. Ensure your app is wrapped in ContentView.")
    }

val LocalAppBarPadding = compositionLocalOf { 0.dp }

private val CaretLeftIcon: ImageVector
    get() =
        ImageVector.Builder(
                name = "CaretLeft",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 256f,
                viewportHeight = 256f
            )
            .apply {
                path(fill = SolidColor(Color.Black)) { // Tint will override the black color
                    moveTo(168.49f, 199.51f)
                    arcToRelative(
                        12f,
                        12f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = true,
                        dx1 = -17f,
                        dy1 = 17f
                    )
                    lineToRelative(-80f, -80f)
                    arcToRelative(
                        12f,
                        12f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = true,
                        dx1 = 0f,
                        dy1 = -17f
                    )
                    lineToRelative(80f, -80f)
                    arcToRelative(
                        12f,
                        12f,
                        0f,
                        isMoreThanHalf = false,
                        isPositiveArc = true,
                        dx1 = 17f,
                        dy1 = 17f
                    )
                    lineTo(97f, 128f)
                    close()
                }
            }
            .build()

@ExperimentalComponentsApi
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable () -> Unit = {}
) {
    val navigator = LocalNavigator.current
    val colors = LocalColors.current
    val slot = LocalAppBarSlot.current

    val appBarContent =
        @Composable {
            Box(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .heightIn(min = APP_BAR_HEIGHT_DEFAULT)
                        .padding(horizontal = 16.dp),
                contentAlignment = UiAlignment.Center
            ) {
                // Title centered
                if (title != null) {
                    Text(
                        text = title,
                        role = TextRole.TitleMedium,
                        weight = TextWeight.Medium,
                    )
                }

                // Leading content (Back Button)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = UiAlignment.CenterVertically
                ) {
                    if (navigator?.canGoBack == true) {
                        Button(
                            onClick = { navigator.pop() },
                            variant = ButtonVariant.Soft,
                            style = ButtonStyle.Primary,
                            size = SizeToken.Small,
                        ) {
                            Icon(
                                imageVector = CaretLeftIcon,
                                contentDescription = "Back",
                                tint = Color(colors.primary),
                                size = 18.dp
                            )
                            Text(
                                text = navigator.previousTitle ?: "Back",
                                role = TextRole.BodyMedium,
                                color = Color(colors.primary)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Custom content, filling the same space if needed
                Box(modifier = Modifier.matchParentSize()) { content() }
            }
        }

    val currentAppBarContent by rememberUpdatedState(appBarContent)

    DisposableEffect(Unit) {
        slot.value = { currentAppBarContent() }
        onDispose { slot.value = null }
    }
}
