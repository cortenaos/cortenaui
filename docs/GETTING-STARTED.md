# Getting Started

**CortenaUI** is a Kotlin Multiplatform design system library specifically designed to build user interfaces for the Cortena operating system (an AOSP-based custom ROM) and its app ecosystem.

## Project Structure

CortenaUI ships as four publishable modules. Each one is independently consumable from Maven Central, and `ui` transitively pulls the other three.

1. **`ui-foundation`** — pure Kotlin design tokens and framework-agnostic shape geometry. Zero external dependencies. Holds raw colors (`Long` ARGB), sizes (`Float`), typography (`Float` sp), motion durations (`Long` ms), and easing curves. Consumable from Compose, Android View system, and AOSP / Android.bp builds without modification.
2. **`ui-shape`** — Compose-aware shape system. Bridges the squircle math from `ui-foundation` to the Compose `Shape` API. Provides `CapsuleShape`, `RoundedShape`, `UnevenShape`, and the `ComponentShape` hierarchy.
3. **`ui-motion`** — spring presets, duration tiers, and easing curves used across CortenaUI. Components read motion specs through `LocalMotion` rather than constructing `spring(...)` or `tween(...)` calls inline.
4. **`ui`** — Compose component layer (`Button`, `Slider`, `Toggle`, `Text`, `Icon`, `ScrollView`, `Theme`, etc.). Transitively pulls the three modules above.

There are **no** dependencies on Material / Material3. Cortena builds its own design language entirely from scratch.

## Prerequisites

CortenaUI is published to **Maven Central** under `io.github.cortenaui`. Maven Central is enabled by default in modern Gradle setups; if you have customised your repositories, make sure it is declared:

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}
```

Add the dependency to your application module's `build.gradle.kts`. The recommended pattern uses a Gradle version catalog so the version stays in one place:

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

If you don't use a version catalog, the inline form works too:

```kotlin
dependencies {
    implementation("io.github.cortenaui:ui:0.3.0-alpha")
}
```

### Build configuration

CortenaUI's `ui` module targets modern Android. Your application module must compile against API 37 or newer:

```kotlin
android {
    compileSdk = 37  // minimum, newer is fine
    defaultConfig {
        minSdk = 35  // required for the ui module
    }
}
```

For details on what each module requires and on cherry-picking individual modules, see [INSTALLATION](INSTALLATION.md).

### A note on Material

CortenaUI is a complete design system. Mixing it with Material 3 in the same screen produces inconsistent visuals — Material's components have their own typography, motion, and shape language that fights with Cortena's. **Use one or the other within a screen**. There are zero Material dependencies in CortenaUI.

### Icons

We recommend using the official `cortenaui-phosphor-icons` library which integrates natively with CortenaUI's `Icon` component.

```toml
# gradle/libs.versions.toml
cortena-icons-phosphor = { module = "io.github.cortenaui:phosphor_icons", version.ref = "cortenaui" }
```

```kotlin
// app/build.gradle.kts
dependencies {
    implementation(libs.cortena.icons.phosphor)
}
```

## How to Use

1. Start your user interface at the root of your `Activity` using **`ContentView`**.
2. **`ContentView`** automatically:
   - Calls `enableEdgeToEdge()` for transparent system bars.
   - Wraps content in **`Theme`** with the requested `themeMode`, palette, typography, fontFamily, sizeToken, and motion.
   - Manages status bar icon contrast (`StatusBarIconMode`).
   - Renders an optional status bar color overlay and an `appBar` slot above the main content.
3. Inside `ContentView`, compose your screen with **`Body`** (edge-to-edge background container), **`ScrollView`** (when content can overflow), **`SafeArea`** (system insets padding), and the rest of the components.

### Minimal Android example

The shape below mirrors the catalog's own `MainActivity`. It is the smallest possible app that uses every layout primitive correctly:

```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import framework.cortena.ui.components.Button
import framework.cortena.ui.components.Text
import framework.cortena.ui.components.TextRole
import framework.cortena.ui.layout.Body
import framework.cortena.ui.layout.ContentView
import framework.cortena.ui.layout.SafeArea
import framework.cortena.ui.layout.ScrollView
import framework.cortena.ui.theme.LocalColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContentView {
            Body {
                ScrollView {
                    SafeArea {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            val colors = LocalColors.current
                            Text(
                                "Hello CortenaUI",
                                color = Color(colors.primary),
                                role = TextRole.TitleMedium,
                            )
                            Button(onClick = { /* ... */ }) {
                                Text("Get started")
                            }
                        }
                    }
                }
            }
        }
    }
}
```

`ContentView` accepts `themeMode` as a state-producer lambda (`() -> ThemeMode`) so a parent can flip the theme without recomposing the entire tree.

### Adding a custom font

Drop a font file into `app/src/main/res/font/` and pass it via `ContentView`:

```kotlin
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

ContentView(
    themeMode = { themeMode.value },
    fontFamily = FontFamily(Font(R.font.your_font)),
) {
    // ...
}
```

Every `Text` component in the tree picks up the new family automatically.

## References

Layout primitives — `docs/layout/`:

- [ContentView](layout/ContentView.md)
- [Theme](layout/Theme.md)
- [Body](layout/Body.md)
- [SafeArea](layout/SafeArea.md)
- [ScrollView](layout/ScrollView.md)
- [LazyScrollView](layout/LazyScrollView.md)
- [GridView](layout/GridView.md)
- [LazyGridView](layout/LazyGridView.md)

Components — `docs/components/`:

- [AppBar](components/AppBar.md)
- [Button](components/Button.md)
- [Icon](components/Icon.md)
- [Separator](components/Separator.md)
- [Slider](components/Slider.md)
- [Text](components/Text.md)
- [Toggle](components/Toggle.md)

Design language — `docs/extra/`:

- [Motion](extra/Motion.md)
- [Shape](extra/Shape.md)

For installation details, modular adoption, and version policy, see [INSTALLATION](INSTALLATION.md).
