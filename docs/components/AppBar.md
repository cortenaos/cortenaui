# AppBar

`AppBar` is the top navigation bar or header component in CortenaUI. This component is purely created by detecting _window insets_ (specifically operating system paddings) so it perfectly adapts to the _Edge-to-Edge_ design.

## Concept

Unlike a standard `TopAppBar` which is unaware of insets unless modified, the Cortena `AppBar` proactively reads the OS's `WindowInsets.statusBars` height and adds it to its default size parameter (`APP_BAR_HEIGHT_DEFAULT` which is 56dp). This means you simply place your layout inside the AppBar, and it will automatically shift down, safely avoiding the _notch_ (the phone's top camera) overlaps.

### Alignment and Layout Slots

The `AppBar` provides a structured layout inspired by standard Jetpack Compose components:

- **Title Alignment**: Automatically adjusts. If a `leading` component (like a back button) is present, the title centers perfectly. If there is no leading component, the title aligns to the `Start` (left).
- **Leading & Trailing**: Dedicated slots for actions. If `leading` is not provided, `AppBar` integrates with `LocalNavigator` to automatically provide a back button if navigating backward is possible.

### Transparent & Customizable Background

By default, the `AppBar` uses a soft vertical gradient fading from the theme's `Surface` color (with 0.9 alpha) to `Transparent` at the bottom. This allows content scrolling beneath the AppBar to elegantly disappear behind a frosted/faded effect. You can override this using a custom `Brush` or `SolidColor`.

## API Reference

```kotlin
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    titleAlignment: Alignment? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    background: Brush? = null,
    content: @Composable () -> Unit = {}
)
```

### Parameters

| Name             | Data Type                            | Description                                                                                                                                                        |
| ---------------- | ------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `modifier`       | `Modifier`                           | Standard Compose modifier. Note that providing `Modifier.background()` here is not recommended as it will interfere with the dedicated `background` parameter.     |
| `title`          | `(@Composable () -> Unit)?`          | Optional composable for the AppBar's title (typically a `Text` component).                                                                                         |
| `titleAlignment` | `Alignment?`                         | Overrides the automatic title alignment. By default, it resolves to `Center` if a leading element (or back button) exists, otherwise `Start`.                      |
| `leading`        | `(@Composable () -> Unit)?`          | Optional composable for the leading edge of the AppBar (left side). If omitted and `navigator.canGoBack` is true, an automatic back button is rendered.            |
| `trailing`       | `(@Composable RowScope.() -> Unit)?` | Optional composable for the trailing edge of the AppBar (right side). Useful for action buttons like search or settings. Items are placed in a `RowScope`.         |
| `background`     | `Brush?`                             | Defines the background of the AppBar. If null, a vertical gradient from `Surface` to `Transparent` is applied. To use a solid color, pass `SolidColor(Color.Red)`. |
| `content`        | `@Composable () -> Unit`             | Custom content elements that overlay the entire AppBar area. Generally unused unless creating highly customized headers.                                           |

## Example

```kotlin
// Basic usage with title and automatic back button
AppBar(
    title = {
        Text("Settings", role = TextRole.TitleMedium, weight = TextWeight.Medium)
    }
)

// With custom leading, trailing, and a solid background
AppBar(
    title = {
        Text("Profile", role = TextRole.TitleMedium, weight = TextWeight.Medium)
    },
    background = SolidColor(Color(LocalColors.current.surface)),
    leading = {
        Button(onClick = { /* Close */ }) {
            Text("Close")
        }
    },
    trailing = {
        Button(onClick = { /* Save action */ }) {
            Text("Save")
        }
    }
)
```

### Navigator Integration

To enable the automatic back button behavior, wrap your application or component tree in a `CompositionLocalProvider` providing a `Navigator` instance:

```kotlin
val navigator = object : Navigator {
    override val canGoBack: Boolean = true
    override val previousTitle: String? = "Catalog"

    override fun pop() {
        // Handle back action
    }
}

CompositionLocalProvider(LocalNavigator provides navigator) {
    // Inside this tree, AppBar will automatically show a back button
    AppBar(
        title = {
            Text("Component Demo", role = TextRole.TitleMedium)
        }
    )
}
```
