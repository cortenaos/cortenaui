/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import framework.cortena.ui.shape.RoundedShape
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.typography.TextWeight

/**
 * Tracker provided by [ListView] to each item via [LocalListItemLeadingTracker]. [ListItem] writes
 * to [hasLeading] during composition so that [ListView] can adjust the separator inset
 * automatically.
 */
internal class ListItemLeadingTracker {
    var hasLeading: Boolean = false
}

/**
 * CompositionLocal that carries the current [ListItemLeadingTracker]. When `null`, the composable
 * is not inside a [ListView] and no tracking occurs.
 */
internal val LocalListItemLeadingTracker =
    staticCompositionLocalOf<ListItemLeadingTracker?> { null }

/**
 * CortenaUI - ListView
 *
 * A rounded, card-like container for a group of [ListItem]s, creating an inset grouped list style.
 * Automatically inserts [Separator] dividers between items.
 *
 * Items are registered via the [ListViewScope.item] builder inside the [content] lambda. Separator
 * insets are determined automatically: when a [ListItem] inside an item has a `leading` slot, the
 * separator below it indents to 56.dp to align with the text. When there is no leading content, the
 * separator uses a symmetrical 16.dp horizontal inset.
 *
 * @param modifier Modifier applied to the outer container.
 * @param title Optional section header, styled as uppercase small text above the card.
 * @param content A [ListViewScope] builder where each [ListViewScope.item] call registers one row.
 */
@Composable
fun ListView(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: ListViewScope.() -> Unit,
) {
    val colors = LocalColors.current
    val scope = ListViewScopeImpl().apply(content)

    Column(modifier = modifier.fillMaxWidth()) {
        if (title != null) {
            Text(
                text = title.uppercase(),
                role = TextRole.BodyMedium,
                weight = TextWeight.Medium,
                color = Color(colors.onSurfaceVariant),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }

        Column(
            modifier =
                Modifier.fillMaxWidth()
                    .clip(RoundedShape(24.dp))
                    .background(Color(colors.surfaceVariant)),
        ) {
            scope.items.forEachIndexed { index, itemContent ->
                val tracker = remember { ListItemLeadingTracker() }
                tracker.hasLeading = false // reset before each composition

                CompositionLocalProvider(LocalListItemLeadingTracker provides tracker) {
                    itemContent()
                }

                if (index < scope.items.lastIndex) {
                    val startInset = if (tracker.hasLeading) 56.dp else 16.dp
                    Separator(modifier = Modifier.padding(start = startInset, end = 16.dp))
                }
            }
        }
    }
}

/** Receiver scope for [ListView] content. Use [item] to register each row. */
interface ListViewScope {
    /** Registers a single row inside the [ListView] - typically a [ListItem]. */
    fun item(content: @Composable () -> Unit)
}

internal class ListViewScopeImpl : ListViewScope {
    val items = mutableListOf<@Composable () -> Unit>()

    override fun item(content: @Composable () -> Unit) {
        items.add(content)
    }
}
