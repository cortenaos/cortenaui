# Installation

CortenaUI is published to **Maven Central** under the `io.github.cortenaos` group ID. Each library module is publishable on its own, so you can pull only what you need.

> Source code lives at [github.com/cortenaos/cortenaui](https://github.com/cortenaos/cortenaui). Imports in your code stay on the `framework.cortena.ui.*` package — only the Maven coordinate uses `io.github.cortenaos`.

## Repository

Maven Central is enabled by default in modern Gradle setups. If you have customised your repositories, add it back:

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

```kotlin
// app/build.gradle.kts
dependencies {
    implementation("io.github.cortenaos:ui:0.1.0-alpha")
}
```

After this single line you can use the entire framework:

```kotlin
import framework.cortena.ui.components.Button
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.Toggle
import framework.cortena.ui.layout.ScrollView
import framework.cortena.ui.theme.Theme
import framework.cortena.ui.shape.CapsuleShape
import framework.cortena.ui.motion.LocalMotion

@Composable
fun App() {
    Theme {
        Button(onClick = { }) { Text("Hello CortenaUI") }
    }
}
```

## Modular adoption

Pull only the modules you need. Useful if you only want the design tokens, just the shape system, or only the motion language without the rest of the component layer.

### Tokens only — `ui-foundation`

Pure Kotlin, zero dependencies. Use this if you want CortenaUI's color / size / typography / motion tokens without any Compose surface.

```kotlin
dependencies {
    implementation("io.github.cortenaos:ui-foundation:0.1.0-alpha")
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
    implementation("io.github.cortenaos:ui-shape:0.1.0-alpha")
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
    implementation("io.github.cortenaos:ui-motion:0.1.0-alpha")
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

- **Min SDK**: 35 for `ui`, 21 for `ui-foundation`, `ui-shape`, and `ui-motion`.
- **Compile SDK**: 37+.
- **Kotlin**: 2.3+.
- **Compose Multiplatform**: 1.10.3+.

## Verifying the install

After syncing, render the catalog snippet:

```kotlin
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import framework.cortena.ui.components.Button
import framework.cortena.ui.components.Text
import framework.cortena.ui.theme.Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                Button(onClick = { /* TODO */ }) {
                    Text("CortenaUI is installed")
                }
            }
        }
    }
}
```

If the button renders with the capsule shape, spring press response, and onPrimary color, you are set.
