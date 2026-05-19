/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.shape

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.lerp

/**
 * Per-corner radius spec in start/end semantics. Layout direction is resolved by the consuming
 * shape, not by this data class.
 */
@Immutable
public data class CornerRadii(
    val topStart: Dp,
    val topEnd: Dp,
    val bottomEnd: Dp,
    val bottomStart: Dp,
)

/** Linearly interpolate each corner radius between [start] and [stop]. */
@Stable
public fun lerp(start: CornerRadii, stop: CornerRadii, fraction: Float): CornerRadii =
    CornerRadii(
        topStart = lerp(start.topStart, stop.topStart, fraction),
        topEnd = lerp(start.topEnd, stop.topEnd, fraction),
        bottomEnd = lerp(start.bottomEnd, stop.bottomEnd, fraction),
        bottomStart = lerp(start.bottomStart, stop.bottomStart, fraction),
    )
