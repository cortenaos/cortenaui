/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.layout.internal

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import framework.cortena.ui.geometry.Orientation
import kotlin.math.abs
import kotlin.math.sign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
internal class BounceOverscrollEffect(
    private val scope: CoroutineScope,
    private val orientation: Orientation,
    // Spring spec used whenever the overscroll offset animates back toward zero. Sourced from
    // LocalMotion at construction site so the release feel stays consistent with other content
    // motion in the framework.
    private val releaseSpec: SpringSpec<Float>,
) : OverscrollEffect {

    // Soft visual cap. The animatable is no longer hard-clamped to this value; instead a rubber
    // band resistance curve makes pulling beyond this point progressively harder.
    private val maxOverscroll = 800f

    // When the offset magnitude exceeds this, auto-release fires while the finger is still down.
    private val autoReleaseThreshold = maxOverscroll * 0.8f

    private val overscrollOffset = Animatable(0f)

    private var snapJob: Job? = null

    // Set true after auto-release fires while the user is still holding the screen. Further drag
    // input is ignored (delegated to the underlying scrollable) until the gesture ends, which
    // prevents oscillation against the same finger position.
    private var autoReleaseLatched = false

    override fun applyToScroll(
        delta: Offset,
        source: NestedScrollSource,
        performScroll: (Offset) -> Offset,
    ): Offset {
        val available = if (orientation == Orientation.Vertical) delta.y else delta.x
        var consumed = 0f

        // Once auto-release has fired, ignore the rest of this gesture entirely. The latch is
        // cleared in applyToFling, which runs when the pointer lifts.
        if (autoReleaseLatched && source == NestedScrollSource.UserInput) {
            return performScroll(delta)
        }

        // Pre-scroll: only intercept if dragging in the OPPOSITE direction of the overscroll.
        if (overscrollOffset.value != 0f && source != NestedScrollSource.SideEffect) {
            val previousSign = overscrollOffset.value.sign
            val availableSign = available.sign

            if (previousSign * availableSign < 0) {
                val newValue = overscrollOffset.value + available * 0.3f

                val clampedNewValue =
                    when {
                        previousSign > 0 -> newValue.coerceAtLeast(0f)
                        previousSign < 0 -> newValue.coerceAtMost(0f)
                        else -> newValue
                    }

                consumed = (clampedNewValue - overscrollOffset.value) / 0.3f

                snapJob?.cancel()
                snapJob = scope.launch { overscrollOffset.snapTo(clampedNewValue) }
            }
        }

        // Pass the remaining delta to the underlying list/scrollable.
        val remainingDelta =
            if (orientation == Orientation.Vertical) {
                delta.copy(y = delta.y - consumed)
            } else {
                delta.copy(x = delta.x - consumed)
            }

        val scrollConsumed = performScroll(remainingDelta)

        // Post-scroll: if the list could not consume all the scroll (hit the bounds), feed it into
        // overscroll with rubber-band resistance.
        val unconsumed = remainingDelta - scrollConsumed
        val availableUnconsumed =
            if (orientation == Orientation.Vertical) unconsumed.y else unconsumed.x

        if (abs(availableUnconsumed) > 1f && source == NestedScrollSource.UserInput) {
            val resistance = rubberBandResistance(abs(overscrollOffset.value))
            val newValue = overscrollOffset.value + availableUnconsumed * resistance

            snapJob?.cancel()
            snapJob = scope.launch { overscrollOffset.snapTo(newValue) }

            // Auto-release: if the user keeps pulling past the threshold, spring back to zero
            // immediately and latch out further drag for this gesture.
            if (abs(newValue) >= autoReleaseThreshold && !autoReleaseLatched) {
                autoReleaseLatched = true
                snapJob?.cancel()
                snapJob =
                    scope.launch {
                        overscrollOffset.animateTo(targetValue = 0f, animationSpec = releaseSpec)
                    }
            }
        }

        return if (orientation == Orientation.Vertical) {
            Offset(scrollConsumed.x, consumed + scrollConsumed.y)
        } else {
            Offset(consumed + scrollConsumed.x, scrollConsumed.y)
        }
    }

    override suspend fun applyToFling(
        velocity: Velocity,
        performFling: suspend (Velocity) -> Velocity,
    ) {
        snapJob?.cancel()
        snapJob = null

        // The pointer has lifted. Allow overscroll on the next gesture again.
        autoReleaseLatched = false

        val availableVelocity = if (orientation == Orientation.Vertical) velocity.y else velocity.x
        var consumedVelocity = 0f

        if (overscrollOffset.value != 0f && availableVelocity != 0f) {
            val previousSign = overscrollOffset.value.sign
            val velocitySign = availableVelocity.sign

            if (previousSign * velocitySign < 0) {
                consumedVelocity = availableVelocity

                val predictedEndValue =
                    exponentialDecay<Float>()
                        .calculateTargetValue(
                            initialValue = overscrollOffset.value,
                            initialVelocity = availableVelocity,
                        )

                if (predictedEndValue.sign == previousSign) {
                    overscrollOffset.animateTo(
                        targetValue = 0f,
                        initialVelocity = availableVelocity,
                        animationSpec = releaseSpec,
                    )
                } else {
                    try {
                        overscrollOffset.animateDecay(
                            initialVelocity = availableVelocity,
                            animationSpec = exponentialDecay(),
                        ) {
                            if (value.sign != previousSign) {
                                consumedVelocity -= this.velocity
                                scope.launch { overscrollOffset.snapTo(0f) }
                            }
                        }
                    } catch (_: Exception) {}
                }
            } else {
                scope.launch {
                    overscrollOffset.animateTo(targetValue = 0f, animationSpec = releaseSpec)
                }
            }
        }

        val remainingVelocity =
            if (orientation == Orientation.Vertical) {
                velocity.copy(y = velocity.y - consumedVelocity)
            } else {
                velocity.copy(x = velocity.x - consumedVelocity)
            }

        val flingConsumed = performFling(remainingVelocity)

        val unconsumedVelocity = remainingVelocity - flingConsumed
        val postFlingAvailable =
            if (orientation == Orientation.Vertical) unconsumedVelocity.y else unconsumedVelocity.x

        if (postFlingAvailable != 0f) {
            overscrollOffset.animateTo(
                targetValue = 0f,
                initialVelocity = postFlingAvailable,
                animationSpec = releaseSpec,
            )
        } else if (overscrollOffset.value != 0f) {
            overscrollOffset.animateTo(targetValue = 0f, animationSpec = releaseSpec)
        }
    }

    override val isInProgress: Boolean
        get() = overscrollOffset.value != 0f

    val overscroll: Modifier
        get() =
            Modifier.graphicsLayer {
                if (orientation == Orientation.Vertical) {
                    translationY = overscrollOffset.value
                } else {
                    translationX = overscrollOffset.value
                }
            }

    // Rubber-band resistance. Returns a multiplier in (0, 0.3] that shrinks as the absolute
    // overscroll grows toward maxOverscroll, so the further the user pulls, the harder it gets.
    // This replaces the previous hard bound on the Animatable, which prevented auto-release from
    // ever firing because the value was clamped.
    private fun rubberBandResistance(absoluteOffset: Float): Float {
        val base = 0.3f
        val ratio = (absoluteOffset / maxOverscroll).coerceIn(0f, 1f)
        // Quadratic falloff: full base near zero, ~25% of base at maxOverscroll.
        val falloff = 1f - 0.75f * ratio * ratio
        return base * falloff
    }
}
