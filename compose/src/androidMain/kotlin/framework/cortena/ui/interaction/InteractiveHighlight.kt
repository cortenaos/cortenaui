/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2026-present The CortenaOS Project
 */
package framework.cortena.ui.interaction

import android.graphics.RuntimeShader
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.util.fastCoerceIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class InteractiveHighlight
actual constructor(
    val animationScope: CoroutineScope,
    color: Color,
    val position: (size: Size, offset: Offset) -> Offset,
) {

    // Bespoke spring specs. Like DampedAnimation, the highlight's press progress and shader
    // position trackers each need a custom dampingRatio / stiffness / visibilityThreshold tuple
    // to stay smooth under raw pointer input. The standard three-tier presets in :motion are not
    // a fit here — gesture physics is the documented exception to "always use LocalMotion".
    private val pressProgressAnimationSpec = spring(0.5f, 300f, 0.001f)
    private val positionAnimationSpec = spring(0.5f, 300f, Offset.VisibilityThreshold)

    private val pressProgressAnimation = Animatable(0f, 0.001f)
    private val positionAnimation =
        Animatable(Offset.Zero, Offset.VectorConverter, Offset.VisibilityThreshold)

    private var startPosition = Offset.Zero
    actual val pressProgress: Float
        get() = pressProgressAnimation.value

    actual val offset: Offset
        get() = positionAnimation.value - startPosition

    private val shader = RuntimeShader(InteractiveHighlightShaderSource)
    private val highlightColor = interactiveHighlightColor(color)

    actual val modifier: Modifier =
        Modifier.drawWithContent {
            drawContent()

            val progress = pressProgressAnimation.value
            if (progress > 0f) {
                drawRect(highlightColor.copy(0.14f * progress), blendMode = BlendMode.SrcOver)
                shader.apply {
                    val position = position(size, positionAnimation.value)
                    setFloatUniform("size", size.width, size.height)
                    setColorUniform("color", highlightColor.copy(0.28f * progress).toArgb())
                    setFloatUniform("radius", size.minDimension * 1.5f)
                    setFloatUniform(
                        "position",
                        position.x.fastCoerceIn(0f, size.width),
                        position.y.fastCoerceIn(0f, size.height),
                    )
                }
                drawRect(ShaderBrush(shader), blendMode = BlendMode.SrcOver)
            }
        }

    actual val gestureModifier: Modifier =
        Modifier.pointerInput(Unit) {
            inspectDragGestures(
                onDragStart = { down ->
                    startPosition = down.position
                    animationScope.launch {
                        launch { pressProgressAnimation.animateTo(1f, pressProgressAnimationSpec) }
                        launch { positionAnimation.snapTo(startPosition) }
                    }
                },
                onDragEnd = {
                    animationScope.launch {
                        launch { pressProgressAnimation.animateTo(0f, pressProgressAnimationSpec) }
                        launch { positionAnimation.animateTo(startPosition, positionAnimationSpec) }
                    }
                },
                onDragCancel = {
                    animationScope.launch {
                        launch { pressProgressAnimation.animateTo(0f, pressProgressAnimationSpec) }
                        launch { positionAnimation.animateTo(startPosition, positionAnimationSpec) }
                    }
                },
            ) { change, _ ->
                animationScope.launch { positionAnimation.snapTo(change.position) }
            }
        }
}
