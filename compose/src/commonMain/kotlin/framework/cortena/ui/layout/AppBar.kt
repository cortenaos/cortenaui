/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment as UiAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import framework.cortena.ui.annotation.ExperimentalComponentsApi
import framework.cortena.ui.components.Icon
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.navigation.LocalNavigator
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.typography.TextWeight

private val APP_BAR_HEIGHT_DEFAULT = 56.dp

private val CaretLeftIcon: ImageVector
    get() = ImageVector.Builder(
        name = "CaretLeft",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 256f,
        viewportHeight = 256f
    ).apply {
        path(fill = SolidColor(Color.Black)) { // Tint will override the black color
            moveTo(168.49f, 199.51f)
            arcToRelative(12f, 12f, 0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                dx1 = -17f,
                dy1 = 17f
            )
            lineToRelative(-80f, -80f)
            arcToRelative(12f, 12f, 0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                dx1 = 0f,
                dy1 = -17f
            )
            lineToRelative(80f, -80f)
            arcToRelative(12f, 12f, 0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                dx1 = 17f,
                dy1 = 17f
            )
            lineTo(97f, 128f)
            close()
        }
    }.build()

@ExperimentalComponentsApi
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable () -> Unit = {}
) {
    val navigator = LocalNavigator.current
    val colors = LocalColors.current

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .heightIn(min = APP_BAR_HEIGHT_DEFAULT),
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
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(role = Role.Button) { navigator.pop() },
                    contentAlignment = UiAlignment.Center
                ) {
                    Icon(
                        imageVector = CaretLeftIcon,
                        contentDescription = "Back",
                        tint = Color(colors.primary),
                        size = 20.dp
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        // Custom content, filling the same space if needed
        Box(modifier = Modifier.matchParentSize()) {
            content()
        }
    }
}
