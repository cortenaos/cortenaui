# Architecture

## Module Graph

```mermaid
graph TD
    catalog["`:catalog`"] --> compose["`:compose`"]
    compose --> shape["`:shape`"]
    compose --> foundation["`:foundation`"]
    shape --> foundation
```

`:catalog` depends on `:compose` (and transitively `:shape` and `:foundation`).
`:compose` depends on `:shape` and `:foundation`. Both are exposed via `api(...)` so consumers do
not need a separate dependency on either.
`:shape` depends on `:foundation` and on `compose.runtime` + `compose.ui`. It can be consumed
standalone by Compose-based apps that only need the shape system.
`:foundation` has zero external dependencies — pure Kotlin.

## Modules

### `:foundation`

**Zero external dependencies. Pure Kotlin.**

The single source of truth for all design tokens **and** for framework-agnostic shape geometry.
Values are primitive types (`Long` for colors, `Float` for sizes), and shape geometry is emitted
through a `CornerPathReceiver` SAM interface so this module can be consumed by Compose, the
Android View system, and AOSP build system (`Android.bp`) without modification.

```
foundation/src/commonMain/kotlin/framework/cortena/ui/
├── color/
│   ├── ColorTokens.kt        # raw ARGB Long values (internal)
│   ├── AdaptiveColor.kt      # light + dark color pair
│   ├── ColorToken.kt         # public adaptive color palette
│   └── Palette.kt            # semantic roles (background, primary, error...)
├── geometry/
│   └── Orientation.kt        # Horizontal / Vertical
├── shape/
│   ├── ContinuousCurvature.kt   # pure-Kotlin squircle path emitter
│   ├── CornerPathReceiver.kt    # sink for path commands
│   ├── CornerStyle.kt           # Circular vs Continuous
│   └── internal/
│       └── CornerBuilder.kt     # squircle bezier solver (kotlin.math only)
├── size/
│   ├── SizeToken.kt          # ExtraSmall / Small / Medium / Large / ExtraLarge
│   └── SizeScale.kt          # raw dp Float values per tier
├── typography/
│   ├── TypeScale.kt          # raw sp Float values
│   └── Typography.kt         # semantic roles (bodyMedium, titleLarge...)
└── spacing/
    └── Spacing.kt            # 4dp grid (Xs=4, Sm=8, Md=16...)
```

The shape APIs in `:foundation` are deliberately framework-agnostic. Non-Compose consumers — for
example a system-level dynamic-island overlay rendered with `android.graphics.Canvas` — implement
[`CornerPathReceiver`](../foundation/src/commonMain/kotlin/framework/cortena/ui/shape/CornerPathReceiver.kt)
and call `ContinuousCurvature.emit(...)` to obtain the same squircle path math used by the
Compose components.

### `:shape`

**Compose-aware shape system.** Depends on `:foundation` and on `compose.runtime` + `compose.ui`.
Bridges the framework-agnostic squircle math from `:foundation` to the Compose `Shape` API and
provides the `ComponentShape` hierarchy used throughout `:compose`. Publishable as a standalone
AAR for Compose apps that only want the shape system.

```
shape/src/commonMain/kotlin/framework/cortena/ui/shape/
├── CapsuleShape.kt           # stadium / pill — radius = minDimension / 2
├── ComponentShape.kt         # sealed Shape contract, exposes Corners
├── CornerRadii.kt            # per-corner Dp record + lerp
├── RectangleShape.kt         # sharp rectangle data object
├── RoundedShape.kt           # uniform Dp rounded rectangle
├── ShapeCopy.kt              # ComponentShape.copy(...) overloads
├── ShapeLerp.kt              # lerp(start, stop, fraction[, style])
├── UnevenShape.kt            # per-corner CornerRadii rounded rectangle
└── internal/
    └── ShapeOutline.kt       # bridge from ContinuousCurvature → Compose Outline / Path
```

### `:compose`

Compose wrappers and theme layer. Depends on `:foundation` and `:shape`.
Converts foundation tokens (`Long`/`Float`) to Compose types (`Color`, `TextUnit`, `Dp`).
Provides `Theme { }` entry point via `CompositionLocalProvider`.

The module is split into `commonMain` (multiplatform-ready) and `androidMain` (Android-specific platform code).

#### `commonMain` — Shared Compose Layer

```
compose/src/commonMain/kotlin/framework/cortena/ui/
├── annotation/
│   └── ExperimentalComponentsApi.kt   # opt-in annotation for unstable APIs
├── components/
│   ├── Button.kt            # interactive button with 5 styles, 2 variants
│   ├── Separator.kt         # horizontal / vertical divider line
│   ├── Slider.kt            # continuous + discrete value slider
│   ├── Text.kt              # semantic text with 15 TextRole variants
│   └── Toggle.kt            # spring-animated switch with drag + tap
├── graphics/
│   └── shadow/
│       ├── Shadow.kt                  # shadow data class (radius, offset, color)
│       ├── ShadowModifier.kt          # Modifier.componentShadow
│       ├── InnerShadow.kt             # inner shadow rendering
│       └── InnerShadowModifier.kt     # Modifier.innerShadow
├── interaction/
│   ├── DampedAnimation.kt            # spring-physics animation driver
│   ├── InteractiveAnimation.kt       # graphicsLayer press/drag transforms
│   ├── InteractiveHighlight.kt       # highlight composable (commonMain)
│   ├── InteractiveHighlightColor.kt  # highlight color utilities
│   └── PressGesture.kt               # inspectDragGestures helper
├── layout/
│   ├── AppBar.kt            # top app bar slot
│   ├── Body.kt              # edge-to-edge root wrapper
│   ├── SafeArea.kt          # system insets padding
│   └── ScrollView.kt        # scrollable content container
└── theme/
    ├── ColorExtensions.kt         # ColorToken.value() helpers
    ├── LocalProviders.kt          # CompositionLocal definitions
    ├── StatusBarIconMode.kt       # Light / Dark / Auto enum
    ├── Theme.kt                   # Theme() composable entry point
    └── ThemeMode.kt               # Light / Dark / Auto enum
```

> Shape source files moved to `:shape`. Component code still imports them via the same package
> path (`framework.cortena.ui.shape.CapsuleShape`, etc.) — only the module boundary changed.

#### `androidMain` — Android Platform Code

```
compose/src/androidMain/kotlin/framework/cortena/ui/
├── graphics/
│   └── shadow/              # platform shadow rendering (RenderNode)
├── interaction/
│   ├── InteractiveHighlight.kt       # Android-specific highlight with shader
│   └── InteractiveHighlightShader.kt # AGSL shader for glow effects
└── layout/
    └── ContentView.kt       # ComponentActivity.ContentView() entry point
```

### `:catalog`

Showcase app. Depends on `:compose` (and transitively `:shape` + `:foundation`).
Used to develop and visually verify all components in a live environment.

```
catalog/src/main/java/app/cortena/ui/catalog/
├── MainActivity.kt          # main activity with theme toggle
└── demo/
    ├── Button.kt            # ButtonDemo()
    ├── Color.kt             # ColorDemo() — responsive palette grid
    ├── ScrollView.kt        # ScrollViewDemo()
    ├── Slider.kt            # SliderDemo()
    ├── Toggle.kt            # ToggleDemo()
    └── Typography.kt        # TypographyDemo() — all 15 TextRoles + advanced features
```

## Theme System

### CompositionLocal Providers

The theme layer exposes five `CompositionLocal` keys, each provided by the `Theme()` composable:

| Key                 | Type         | Default             | Purpose                                       |
| ------------------- | ------------ | ------------------- | --------------------------------------------- |
| `LocalIsDark`       | `Boolean`    | `false`             | Whether the current theme is dark mode        |
| `LocalColors`       | `Palette`    | `LightPalette`      | Semantic color roles for the active theme     |
| `LocalContentColor` | `Color?`     | `null`              | Inherited foreground color (scoped by parent) |
| `LocalTypography`   | `Typography` | `DefaultTypography` | Semantic text style scales                    |
| `LocalSpacing`      | `Spacing`    | `Spacing`           | 4dp-grid spacing tokens                       |

### ThemeMode Resolution

`ThemeMode.Auto` delegates to `isSystemInDarkTheme()` inside the `Theme()` composable,
ensuring consistent behavior without manual system-theme checks in outer layouts.

### ContentView Entry Point (Android)

`ComponentActivity.ContentView()` is the recommended Android entry point. It:

1. Calls `enableEdgeToEdge()` for transparent system bars.
2. Wraps content inside `Theme()` with the provided `themeMode`, `palette`, and `typography`.
3. Manages status bar icon contrast (`StatusBarIconMode`) reactively via `SideEffect`.
4. Renders an optional status bar color overlay and `appBar` slot above the main content.

## Component Design Patterns

### Interaction Model

All interactive components (Button, Slider, Toggle) share a common physics-based interaction system:

1. **`DampedAnimation`** — Central animation driver with spring-physics for position, press feedback (scale), and velocity tracking. Uses `Job`-tracked coroutines to prevent stale animation conflicts on rapid successive interactions.
2. **`inspectDragGestures`** — Low-level gesture detector that emits `onDragStart`, `onDrag`, `onDragEnd`, and `onDragCancel` callbacks without consuming pointer events (allowing parent scroll to still function).
3. **`applyInteractiveAnimation`** — `graphicsLayer` extension that applies press-scale, translation, and feedback transforms in a single pass.
4. **`InteractiveHighlight`** — Platform-specific glow/highlight effect (AGSL shader on Android).

### Color Resolution

Components follow a consistent pattern for resolving colors:

```kotlin
val resolvedColor = if (customColor.isSpecified) customColor else Color(colors.semanticRole)
```

This allows per-instance color overrides while defaulting to theme-aware semantic roles.

### Shadow System

The `componentShadow` modifier renders drop shadows that remain visible during scale animations (unlike standard `Modifier.shadow` which clips at the original bounds). This is critical for interactive components like Toggle and Slider indicators.

## Documentation

Component documentation is maintained in `docs/components/` following a standardized format:

| Document         | Component                    |
| ---------------- | ---------------------------- |
| `AppBar.md`      | Top app bar                  |
| `Body.md`        | Edge-to-edge root wrapper    |
| `Button.md`      | Interactive button           |
| `ContentView.md` | Android activity entry point |
| `SafeArea.md`    | System insets padding        |
| `ScrollView.md`  | Scrollable container         |
| `Separator.md`   | Visual divider line          |
| `Shape.md`       | Shape system + squircle math |
| `Slider.md`      | Value adjustment slider      |
| `Text.md`        | Semantic text component      |
| `Theme.md`       | Theme composable             |
| `Toggle.md`      | Switch / toggle              |

Each document follows the structure: **Concept → API Reference → Parameters Table → Examples**.

## Design Decisions

**Why are colors stored as Long?**
`androidx.compose.ui.graphics.Color` is a Compose type. Storing raw `Long` in
`:foundation` means the token layer has zero dependency on Compose — it can
be referenced from `Android.bp` builds for ROM integration without pulling
in the entire Compose runtime.

**Why a separate `:shape` module?**
The squircle math itself is pure Kotlin and lives in `:foundation`. The Compose-binding layer
(`ComponentShape`, `RoundedShape`, etc.) sits in `:shape` so that consumers who want only the
shape system — for example a CortenaOS dynamic-island overlay or a third-party Compose app —
can depend on `:shape` without pulling the rest of CortenaUI. This also keeps the boundary
between framework-agnostic geometry and Compose types explicit and enforceable.

**Why a separate `:compose` module?**
When ROM integration comes, `:foundation` goes into the system image. Compose
components live in apps. Keeping them in separate modules makes that boundary
explicit and enforceable by the build system.

**Why `commonMain` / `androidMain` split in `:compose`?**
The component APIs, shapes, and theme logic are multiplatform-ready in `commonMain`.
Platform-specific code (edge-to-edge window management, AGSL shaders, `RenderNode` shadows)
lives in `androidMain`. This separation enables future targets (Desktop, iOS) with
minimal refactoring.

**Why `DampedAnimation` instead of `Animatable` directly?**
`DampedAnimation` encapsulates the full interactive lifecycle (press → drag → release)
with coordinated spring animations for position, scale, and velocity. It manages
coroutine job tracking internally so components don't need to handle animation
cancellation logic, preventing thumb-stuck bugs during rapid interactions.
