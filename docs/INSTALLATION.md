# Installation

CortenaUI is published to **Maven Central** under the `io.github.cortenaui` group ID. Each library module is publishable on its own, so you can pull only what you need.

> Source code lives at [github.com/cortenaui/cortenaui](https://github.com/cortenaui/cortenaui). Imports in your code stay on the `framework.cortena.ui.*` package — only the Maven coordinate uses `io.github.cortenaui`.

## Repository

Maven Central is enabled by default in modern Gradle setups. If you have customised your repositories, declare it explicitly:

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}
```

## All-in-one — `ui`

The most common case. The `ui` artifact transitively pulls `ui-foundation`, `ui-shape`, and `ui-motion`, so you get the full component library with one dependency.

The recommended pattern uses a Gradle version catalog so the version stays in one place across multi-module apps:

```toml
# gradle/libs.versions.toml
[versions]
cortenaui = "0.3.0-alpha"

[libraries]
cortena-ui = { module = "io.github.cortenaui:ui", version.ref = "cortenaui" }
```

```kotlin
// app/build.gradle.kts
dependencies {
    implementation(libs.cortena.ui)
}
```

If you don't use a version catalog, the inline form works just as well:

```kotlin
dependencies {
    implementation("io.github.cortenaui:ui:0.3.0-alpha")
}
```

After this single line you can use the entire framework. The minimal Android entry point looks like this — note the `ContentView → Body → ScrollView → SafeArea` layering, which is the canonical pattern (see [GETTING-STARTED](GETTING-STARTED.md) for a fuller walkthrough):

```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import framework.cortena.ui.components.Button
import framework.cortena.ui.components.Text
import framework.cortena.ui.layout.Body
import framework.cortena.ui.layout.ContentView
import framework.cortena.ui.layout.SafeArea
import framework.cortena.ui.layout.ScrollView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContentView {
            Body {
                ScrollView {
                    SafeArea {
                        Button(onClick = { /* ... */ }) {
                            Text("Hello CortenaUI")
                        }
                    }
                }
            }
        }
    }
}
```

## Modular adoption

Pull only the modules you need. Useful if you only want the design tokens, just the shape system, or only the motion language without the rest of the component layer.

### Tokens only — `ui-foundation`

Pure Kotlin, zero dependencies. Use this if you want CortenaUI's color / size / typography / motion tokens without any Compose surface.

```kotlin
dependencies {
    implementation("io.github.cortenaui:ui-foundation:0.3.0-alpha")
}
```

```kotlin
import framework.cortena.ui.color.ColorToken
import framework.cortena.ui.size.SizeToken
import framework.cortena.ui.typography.TypeScale
import framework.cortena.ui.motion.DurationTokens
```

### Shapes only — `ui-shape`

Compose `Shape` adapter for CortenaUI's squircle math. Useful for adopters who want the squircle / continuous-curvature look in their own Compose components without pulling in the rest of the framework. Transitively pulls `ui-foundation`.

```kotlin
dependencies {
    implementation("io.github.cortenaui:ui-shape:0.3.0-alpha")
}
```

```kotlin
import framework.cortena.ui.shape.CapsuleShape
import framework.cortena.ui.shape.RoundedShape
import framework.cortena.ui.shape.UnevenShape
```

### Motion only — `ui-motion`

Spring presets, duration tiers, and easing curves used across CortenaUI. Adopt this if you want consistent motion language in your own components without using CortenaUI components themselves. Transitively pulls `ui-foundation`.

```kotlin
dependencies {
    implementation("io.github.cortenaui:ui-motion:0.3.0-alpha")
}
```

```kotlin
import framework.cortena.ui.motion.LocalMotion
import framework.cortena.ui.motion.DefaultMotion
```

## Module dependency graph

```
ui ──api──┐
          ├──► ui-foundation
ui-shape ─api─┤
          │
ui-motion api─┘
```

Direct dependencies you can declare:

| You depend on   | You also get (transitively)              |
| --------------- | ---------------------------------------- |
| `ui-foundation` | nothing                                  |
| `ui-shape`      | `ui-foundation`                          |
| `ui-motion`     | `ui-foundation`                          |
| `ui`            | `ui-foundation`, `ui-shape`, `ui-motion` |

> Each module is a Kotlin Multiplatform publication. Always declare the artifact without a platform suffix (e.g. `ui`, not `ui-android`). Gradle reads the metadata and resolves the right variant for your target — typically the Android AAR — automatically.

## Snapshot builds

There are no snapshot builds during the alpha phase. Each `0.x.0-alpha` tag is a stable point release.

## Manual AAR install

Each release on GitHub also attaches per-module AARs as assets:

- `ui-foundation-<version>.aar`
- `ui-shape-<version>.aar`
- `ui-motion-<version>.aar`
- `ui-<version>.aar`

Drop them into your project's `libs/` folder if you need an offline-friendly install. Note that with this approach you lose Maven's transitive dependency resolution — you must include every AAR your chosen module depends on.

## Requirements

- **Compile SDK**: 37 (Android 17) or newer. Required for the `ui` module.
- **Min SDK**: 35 for `ui`, 21 for `ui-foundation`, `ui-shape`, and `ui-motion`.
- **Kotlin**: 2.3+.
- **Compose Multiplatform**: 1.10.3+.

The `ui` module's `compileSdk = 37` is the **minimum**, not an exact pin — newer versions are supported. If your application module currently builds against a lower SDK, bump `compileSdk` in `app/build.gradle.kts`:

```kotlin
android {
    compileSdk = 37  // minimum, newer is fine
    defaultConfig {
        minSdk = 35  // required for the ui module
    }
}
```

## Mixing with Material 3

CortenaUI is a complete design system. Mixing it with Material 3 in the same screen produces visual inconsistencies — Material's components have their own typography, motion, and shape language that competes with Cortena's. **Pick one or the other per screen**.

The catalog imports vector assets from `androidx.compose.material.icons` (e.g. `Icons.Default.Add`) and feeds them into Cortena's own `Icon` composable. That is the one safe touchpoint: Material as a vector source, never as a renderer.

```kotlin
// Recommended
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import framework.cortena.ui.components.Icon

Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
```

```kotlin
// Avoid
import androidx.compose.material3.Icon  // Material's renderer
import androidx.compose.material3.Button // Material's component
```

## Verifying the install

After syncing, the smallest sanity-check screen looks like this:

```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import framework.cortena.ui.components.Button
import framework.cortena.ui.components.Text
import framework.cortena.ui.layout.Body
import framework.cortena.ui.layout.ContentView
import framework.cortena.ui.layout.SafeArea

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContentView {
            Body {
                SafeArea {
                    Button(onClick = { }) {
                        Text("CortenaUI is installed")
                    }
                }
            }
        }
    }
}
```

If the button renders with the capsule shape, spring press response, and theme-aware colors — and the system bars look correctly handled (status bar icons readable, edge-to-edge layout) — the install is sound.
