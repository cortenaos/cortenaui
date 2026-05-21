/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import framework.cortena.ui.theme.LocalContentColor
import framework.cortena.ui.theme.LocalIconSize
import androidx.compose.foundation.layout.size as foundationSize
import framework.cortena.ui.theme.LocalColors

/**
 * CortenaUI — Icon
 *
 * Renders an [ImageVector] as a tinted icon with a default size resolved from
 * [LocalIconSize]. When this composable is used inside a sized component scope (for example a
 * [Button] content slot), the local icon size automatically scales with the parent's
 * [framework.cortena.ui.size.SizeToken]. Outside such a scope it falls back to a sensible
 * default (24.dp).
 *
 * Tint resolution mirrors [Text]: [LocalContentColor] when set, else `LocalColors.onBackground`.
 *
 * Built on `compose.foundation.Image` rather than Material's `Icon`, so this stays inside the
 * Material-free constraint of `:compose`.
 */
@Composable
fun Icon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = Dp.Unspecified,
    enabled: Boolean = true,
) {
    val colors = LocalColors.current
    val localContentColor = LocalContentColor.current
    val resolvedSize = if (size != Dp.Unspecified) size else LocalIconSize.current
    val resolvedTint =
        when {
            tint.isSpecified -> tint
            localContentColor != null && localContentColor.isSpecified -> localContentColor
            else -> Color(colors.onBackground)
        }

    val painter: Painter = rememberVectorPainter(image = imageVector)

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier.foundationSize(resolvedSize).alpha(if (enabled) 1f else 0.38f),
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.tint(resolvedTint),
    )
}
