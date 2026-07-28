/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import framework.cortena.ui.theme.LocalColors

/**
 * A standard list item row typically used inside a [ListView].
 *
 * Provides structured slots for leading content (like icons or avatars), the main title, an
 * optional subtitle, and trailing content (like chevrons, toggles, or values).
 *
 * @param title The primary text or content of the item.
 * @param modifier Modifier applied to the outer container.
 * @param subtitle Optional secondary text below the title.
 * @param leading Optional content placed at the start of the item (e.g., Icon).
 * @param trailing Optional content placed at the end of the item (e.g., Toggle).
 * @param onClick Optional callback when the item is clicked. If provided, the item becomes
 *   clickable.
 */
@Composable
fun ListItem(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val colors = LocalColors.current

    // Report leading presence to ListView's tracker (if inside a ListView)
    LocalListItemLeadingTracker.current?.let { it.hasLeading = leading != null }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 52.dp)
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Leading slot
        if (leading != null) {
            Box(contentAlignment = Alignment.Center) { leading() }
            Spacer(modifier = Modifier.width(16.dp))
        }
        // Main content (Title + Subtitle)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            title()
            if (subtitle != null) {
                subtitle()
            }
        }
        // Trailing slot
        if (trailing != null) {
            Spacer(modifier = Modifier.width(16.dp))
            trailing()
        }
    }
}
