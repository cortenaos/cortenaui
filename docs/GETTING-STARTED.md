# Getting Started

**CortenaUI** is a Kotlin Multiplatform design system library specifically designed to build user interfaces for the Cortena operating system (an AOSP-based custom ROM) and its app ecosystem.

## Project Structure

This library is divided into two main layers to ensure platform independence and a strict design language:

1. **`:foundation`**: A pure Kotlin layer with no dependencies on any UI framework. It contains raw design tokens like colors (stored as `Long` ARGB), typography sizes (`Float`), spacing (`Float`), and shape corner radii (`Float`).
2. **`:compose`**: A Jetpack Compose implementation layer that translates tokens from `:foundation` to standard Compose UI objects (like `Color`, `.sp`, `.dp`). There are **no** dependencies on Material / Material3 here, as Cortena builds its own custom design system entirely from scratch.

## Prerequisites

Add the `:compose` module dependency (which internally includes `:foundation`) to the `build.gradle.kts` in your application module:

```kotlin
implementation(project(":compose"))
```

## How to Use

1. Start your user interface at the root of your `Activity` using **`ContentView`**.
2. **`ContentView`** will automatically:
   - Call `enableEdgeToEdge()`.
   - Redraw the status bar color as needed.
   - Enable status bar icons contrast detection.
   - Wrap its content using **`Theme`**.
3. Compose your UI using `Body`, `SafeArea`, `AppBar`, `Text`, and so on.

### Simple Implementation Example (Android)

```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import framework.cortena.ui.components.Text
import framework.cortena.ui.layout.AppBar
import framework.cortena.ui.layout.Body
import framework.cortena.ui.layout.ContentView
import framework.cortena.ui.layout.SafeArea
import framework.cortena.ui.theme.StatusBarIconMode
import framework.cortena.ui.theme.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // The root of the application, automatically handles Edge-to-Edge and Theme Injector
        ContentView(
            appBar = {
                AppBar(modifier = Modifier.background(Color.DarkGray)) {}
            },
            statusBarColor = Color.DarkGray,
            statusBarIconMode = StatusBarIconMode.Auto, // Will automatically calculate light/dark icons
            themeMode = ThemeMode.Auto
        ) {
            Body(modifier = Modifier.background(Color.Black)) {
                SafeArea {
                    Text(
                        text = "Welcome to Cortena UI!",
                        color = Color.White
                    )
                }
            }
        }
    }
}
```

## References

Visit the layout guides individually in the `docs/layout/` directory:

- [ContentView](layout/ContentView.md)
- [Theme](layout/Theme.md)
- [Body](layout/Body.md)
- [SafeArea](layout/SafeArea.md)
- [ScrollView](layout/ScrollView.md)

Visit the component guides individually in the `docs/components/` directory:

- [AppBar](components/AppBar.md)
- [Button](components/Button.md)
- [Separator](components/Separator.md)
- [Slider](components/Slider.md)
- [Text](components/Text.md)
- [Toggle](components/Toggle.md)

Visit the extra guides individually in the `docs/extra/` directory:

- [Motion](extra/Motion.md)
- [Shape](extra/Shape.md)
