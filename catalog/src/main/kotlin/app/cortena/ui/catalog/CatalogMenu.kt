/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package app.cortena.ui.catalog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import framework.cortena.icons.PhosphorIcon
import framework.cortena.icons.PhosphorIcons
import framework.cortena.ui.components.Icon
import framework.cortena.ui.components.ListItem
import framework.cortena.ui.components.ListView
import framework.cortena.ui.components.ListViewScope
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.theme.LocalColors

@Composable
fun CatalogMenu(onNavigate: (String) -> Unit) {
    val colors = LocalColors.current

    ListView(title = "Components") {
        catalogMenuItem("Button", onNavigate)
        catalogMenuItem("Icon", onNavigate)
        catalogMenuItem("Slider", onNavigate)
        catalogMenuItem("Toggle", onNavigate)
        catalogMenuItem("ListView", onNavigate)
    }

    Spacer(modifier = Modifier.height(8.dp))

    ListView(title = "Layout") {
        catalogMenuItem("ScrollView", onNavigate)
        catalogMenuItem("LazyScrollView", onNavigate)
        catalogMenuItem("GridView", onNavigate)
        catalogMenuItem("LazyGridView", onNavigate)
    }

    Spacer(modifier = Modifier.height(8.dp))

    ListView(title = "Theme") {
        catalogMenuItem("Typography", onNavigate)
        catalogMenuItem("Colors", onNavigate)
    }
}

private fun ListViewScope.catalogMenuItem(title: String, onNavigate: (String) -> Unit) {
    item {
        ListItem(
            title = { Text(title, role = TextRole.BodyMedium) },
            trailing = {
                Icon(
                    renderer = PhosphorIcon(PhosphorIcons.Bold.CaretRight),
                    contentDescription = null,
                    tint = Color(LocalColors.current.onSurfaceVariant),
                    size = 14.dp,
                )
            },
            onClick = { onNavigate(title) },
        )
    }
}
