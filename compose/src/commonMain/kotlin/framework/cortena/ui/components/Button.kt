/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import framework.cortena.ui.interaction.InteractiveHighlight
import framework.cortena.ui.interaction.applyInteractiveAnimation
import framework.cortena.ui.shape.CapsuleShape
import framework.cortena.ui.size.SizeToken
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.theme.LocalContentColor
import framework.cortena.ui.theme.LocalIconSize
import framework.cortena.ui.theme.LocalSizeToken
import framework.cortena.ui.theme.LocalSpacing
import framework.cortena.ui.theme.LocalTextRole
import framework.cortena.ui.theme.LocalTextWeight
import framework.cortena.ui.theme.componentHeight
import framework.cortena.ui.theme.contentGap
import framework.cortena.ui.theme.horizontalPadding
import framework.cortena.ui.theme.iconSize
import framework.cortena.ui.typography.TextWeight

enum class ButtonStyle {
    Primary,
    Secondary,
    Accent,
    Ghost,
    Destructive,
}

enum class ButtonVariant {
    Default,
    Soft,
}

@Suppress("ModifierParameter")
@Composable
fun Button(
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    iconOnly: Boolean = false,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactive: Boolean = true,
    size: SizeToken = LocalSizeToken.current,
    style: ButtonStyle = ButtonStyle.Primary,
    variant: ButtonVariant = ButtonVariant.Default,
    background: Color = Color.Unspecified,
    foreground: Color = Color.Unspecified,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = LocalColors.current
    val spacing = LocalSpacing.current
    val animationScope = rememberCoroutineScope()

    val primary = Color(colors.primary)
    val secondary = Color(colors.secondary)
    val accent = Color(colors.accent)
    val error = Color(colors.error)
    val onSurfaceVariant = Color(colors.onSurfaceVariant)

    val styleBackgroundColor =
        when (variant) {
            ButtonVariant.Default ->
                when (style) {
                    ButtonStyle.Primary -> primary
                    ButtonStyle.Secondary -> secondary
                    ButtonStyle.Accent -> accent
                    ButtonStyle.Ghost -> secondary.copy(alpha = 0.20f)
                    ButtonStyle.Destructive -> error
                }

            ButtonVariant.Soft ->
                when (style) {
                    ButtonStyle.Primary -> primary.copy(alpha = 0.12f)
                    ButtonStyle.Secondary -> secondary.copy(alpha = 0.12f)
                    ButtonStyle.Accent -> accent.copy(alpha = 0.12f)
                    ButtonStyle.Ghost -> secondary.copy(alpha = 0.08f)
                    ButtonStyle.Destructive -> secondary.copy(alpha = 0.12f)
                }
        }

    val styleForegroundColor =
        when (variant) {
            ButtonVariant.Default ->
                when (style) {
                    ButtonStyle.Primary -> Color(colors.onPrimary)
                    ButtonStyle.Secondary -> Color.White
                    ButtonStyle.Accent -> Color.White
                    ButtonStyle.Ghost -> onSurfaceVariant
                    ButtonStyle.Destructive -> Color(colors.onError)
                }

            ButtonVariant.Soft ->
                when (style) {
                    ButtonStyle.Primary -> primary
                    ButtonStyle.Secondary -> secondary
                    ButtonStyle.Accent -> accent
                    ButtonStyle.Ghost -> onSurfaceVariant
                    ButtonStyle.Destructive -> error
                }
        }

    val backgroundColor = if (background.isSpecified) background else styleBackgroundColor
    val contentColor = if (foreground.isSpecified) foreground else styleForegroundColor

    // Map the button's size tier onto a default Text role for content. User can still override
    // by passing role / weight directly to Text inside the content lambda.
    val contentTextRole =
        when (size) {
            SizeToken.Small -> TextRole.BodySmall
            SizeToken.Medium -> TextRole.BodyMedium
            SizeToken.Large -> TextRole.BodyLarge
        }

    // Changes to the background color must not destroy and recreate the InteractiveHighlight
    val interactiveHighlight =
        remember(animationScope) {
            InteractiveHighlight(animationScope = animationScope, color = backgroundColor)
        }

    Row(
        modifier =
            modifier
                .graphicsLayer {
                    if (interactive) {
                        applyInteractiveAnimation(
                            pressProgress = interactiveHighlight.pressProgress,
                            offset = interactiveHighlight.offset,
                        )
                    }
                    alpha = if (enabled) 1f else 0.38f
                }
                .clip(CapsuleShape())
                .background(backgroundColor)
                .then(if (enabled) interactiveHighlight.gestureModifier else Modifier)
                .combinedClickable(
                    interactionSource = null,
                    indication = null,
                    enabled = enabled,
                    //hapticFeedbackEnabled = false,
                    role = Role.Button,
                    onClick = { onClick?.invoke() },
                    onLongClick = { onLongClick?.invoke() },
                )
                .then(if (enabled) interactiveHighlight.modifier else Modifier)
                .height(size.componentHeight)
                .then(
                    if (iconOnly) Modifier.width(size.componentHeight).padding(spacing.Xs.dp)
                    else Modifier.padding(horizontal = size.horizontalPadding)
                ),
        horizontalArrangement =
            Arrangement.spacedBy(space = size.contentGap, alignment = Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextRole provides contentTextRole,
            LocalTextWeight provides TextWeight.Default,
            LocalIconSize provides size.iconSize,
        ) {
            content()
        }
    }
}
