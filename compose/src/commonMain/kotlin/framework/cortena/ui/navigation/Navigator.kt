/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.navigation

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * A basic interface for navigation state that UI components (like AppBar) can consume.
 */
interface Navigator {
    /**
     * Whether the navigator has history and can navigate back.
     */
    val canGoBack: Boolean

    /**
     * Navigates back to the previous screen.
     */
    fun pop()
}

/**
 * CompositionLocal containing the current [Navigator].
 * If null, the app is not utilizing CortenaUI's built-in automatic navigation features.
 */
val LocalNavigator = staticCompositionLocalOf<Navigator?> { null }
