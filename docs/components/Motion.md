# Motion

CortenaUI's motion language. A small, opinionated set of spring presets, duration tiers, and easing curves shared by every interactive component. Read at runtime through `LocalMotion.current`.

## Concept

Motion is a design token, just like color or spacing. Components do not invent their own animation specs — they pick a preset that matches the role of the motion. This keeps the feel of the library coherent across components and across the surface as a whole.

The system distinguishes two kinds of motion:

- **Spring presets** are the default for any motion that originates from user input or follows physical causality — press response, content shift, panel reveal. Spring fits because the user feels the physics.
- **Tween durations + easing** are reserved for transitions that must be deterministic — alpha fade, color crossfade, indicator hide. Tween fits because the timing is what matters, not the physics.

Components must read both through `LocalMotion.current` and never call `spring(...)` or `tween(...)` with hardcoded numbers. The single documented exception is raw gesture-physics primitives (`DampedAnimation`, `InteractiveHighlight`), which need bespoke per-track specs to feel right under continuous pointer input.

## API Reference

```kotlin
@Immutable
class Motion {
    val snappy: SpringSpec<Float>
    val smooth: SpringSpec<Float>
    val gentle: SpringSpec<Float>

    val fast: Int     // 150 ms
    val medium: Int   // 250 ms
    val slow: Int     // 450 ms

    val standardEasing: Easing
    val emphasizedEasing: Easing
    val linearEasing: Easing
}

val DefaultMotion: Motion
val LocalMotion: ProvidableCompositionLocal<Motion>
```

### Spring Presets

| Preset   | Stiffness                   | Damping ratio | Use case                                                |
| -------- | --------------------------- | ------------- | ------------------------------------------------------- |
| `snappy` | `Spring.StiffnessHigh`      | No bouncy     | Tight UI feedback (press, toggle thumb, indicator drag) |
| `smooth` | `Spring.StiffnessMediumLow` | No bouncy     | General content shift, panel reveal, item move          |
| `gentle` | `Spring.StiffnessLow`       | No bouncy     | Large overlays, dialog enter / exit, page transition    |

All three default to `DampingRatioNoBouncy`. Components that want a tactile overshoot — for example a button releasing from a press — should construct a custom `spring()` from these stiffness values plus a higher damping ratio (`DampingRatioMediumBouncy`) rather than redefining the stiffness.

### Duration Tokens

| Token    | Value | Use case                                          |
| -------- | ----- | ------------------------------------------------- |
| `fast`   | 150   | Tap feedback, small fades, color shifts           |
| `medium` | 250   | Standard fades, indicator hide, content crossfade |
| `slow`   | 450   | Emphasis transitions, dialog scrim                |

Durations are exposed as `Int` (ms) so they pass directly into `tween()`.

### Easings

| Easing             | Curve                  | Use case                        |
| ------------------ | ---------------------- | ------------------------------- |
| `standardEasing`   | `(0.4, 0.0, 0.2, 1.0)` | Default for most fades          |
| `emphasizedEasing` | `(0.2, 0.0, 0.0, 1.0)` | Decelerated arrivals            |
| `linearEasing`     | linear                 | Color / alpha that must be even |

## Examples

Spring-driven content motion:

```kotlin
val motion = LocalMotion.current
val offset by animateDpAsState(targetOffset, animationSpec = motion.smooth)
```

Tap feedback that should feel instant:

```kotlin
val motion = LocalMotion.current
val scale by animateFloatAsState(if (pressed) 0.96f else 1f, animationSpec = motion.snappy)
```

Deterministic alpha fade:

```kotlin
val motion = LocalMotion.current
val alpha = remember { Animatable(0f) }
LaunchedEffect(visible) {
    alpha.animateTo(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(motion.medium, easing = motion.standardEasing),
    )
}
```

Custom tactile overshoot — the documented pattern for exceptions like indicator drag-thicken:

```kotlin
val motion = LocalMotion.current
val scaleSpring =
    spring<Float>(
        stiffness = motion.smooth.stiffness,
        dampingRatio = Spring.DampingRatioMediumBouncy,
    )
```

## Override

Provide a custom `Motion` to `Theme()` if you want to tune the language for a specific surface — for example a "reduce motion" accessibility mode that swaps every spring for a faster, less bouncy variant:

```kotlin
Theme(motion = MyReducedMotion) {
    // ...
}
```
