/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import framework.cortena.ui.shape.RoundedShape
import framework.cortena.ui.theme.LocalColors
import framework.cortena.ui.typography.TextWeight

/**
 * CortenaUI - ListView
 *
 * A rounded, card-like container for a group of [ListItem]s, creating an inset grouped list style
 * similar to iOS Settings pages. Automatically inserts [Separator] dividers between items.
 *
 * Items are registered via the [ListViewScope.item] builder inside the [content] lambda. Separators
 * are drawn between each pair of items using the provided [separatorPadding].
 *
 * @param modifier Modifier applied to the outer container.
 * @param title Optional section header, styled as uppercase small text above the card.
 * @param footer Optional section footer text below the card.
 * @param showSeparators When `true` (default), draws a [Separator] between each pair of items.
 * @param separatorPadding Padding applied to each auto-inserted separator. The default value
 *   creates a symmetrical horizontal inset.
 * @param content A [ListViewScope] builder where each [ListViewScope.item] call registers one row.
 */
@Composable
fun ListView(
    modifier: Modifier = Modifier,
    title: String? = null,
    footer: String? = null,
    showSeparators: Boolean = true,
    separatorPadding: PaddingValues = PaddingValues(start = 56.dp, end = 16.dp),
    content: ListViewScope.() -> Unit,
) {
    val colors = LocalColors.current
    val scope = ListViewScopeImpl().apply(content)

    Column(modifier = modifier.fillMaxWidth()) {
        if (title != null) {
            Text(
                text = title.uppercase(),
                role = TextRole.BodySmall,
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
            scope.items.forEachIndexed { index, item ->
                item()
                if (showSeparators && index < scope.items.lastIndex) {
                    Separator(modifier = Modifier.padding(separatorPadding))
                }
            }
        }

        if (footer != null) {
            Text(
                text = footer,
                role = TextRole.BodySmall,
                color = Color(colors.onSurfaceVariant),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }
}

/** Receiver scope for [ListView] content. Use [item] to register each row. */
interface ListViewScope {
    /** Registers a single row inside the [ListView]. */
    fun item(content: @Composable () -> Unit)
}

internal class ListViewScopeImpl : ListViewScope {
    val items = mutableListOf<@Composable () -> Unit>()

    override fun item(content: @Composable () -> Unit) {
        items.add(content)
    }
}
