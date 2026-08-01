# AppBar

`AppBar` is the top navigation bar or header component in CortenaUI. This component is purely created by detecting _window insets_ (specifically operating system paddings) so it perfectly adapts to the _Edge-to-Edge_ design.

!!! warning "Experimental API"

    This component is annotated with `@ExperimentalComponentsApi` and its API is subject to change.

## Concept

Unlike a standard `TopAppBar` which is unaware of insets unless modified, the Cortena `AppBar` proactively reads the OS's `WindowInsets.statusBars` height and adds it to its default size parameter (`APP_BAR_HEIGHT_DEFAULT` which is 56dp). This means you simply place your layout inside the AppBar, and it will automatically shift down, safely avoiding the _notch_ (the phone's top camera) overlaps.

### Automatic Back Button (Navigator)

`AppBar` tightly integrates with Cortena's `LocalNavigator`. If `LocalNavigator` is provided in the composition hierarchy, and its state dictates that `canGoBack` is `true`, `AppBar` will automatically render a leading **Back** button. The back button uses a `ButtonVariant.Soft` styling and automatically falls back to reading the `previousTitle` from the `Navigator` interface.

## API Reference

```kotlin
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable () -> Unit = {}
)
```

### Parameters

| Name       | Data Type                | Description                                                                                                                                                                                                                                  |
| ---------- | ------------------------ | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `modifier` | `Modifier`               | Standard Compose modifier. Typically filled with modifiers like additional vertical padding or _background_ color. _Important: If providing Modifier.background, the rendered color will also draw into the statusBar boundaries (overlap)_. |
| `title`    | `String?`                | Optional text title for the AppBar. It is styled with `TitleMedium` and automatically centered horizontally and vertically inside the AppBar.                                                                                                |
| `content`  | `@Composable () -> Unit` | The content elements of the AppBar, such as custom actions, dropdown menus, etc.                                                                                                                                                             |

## Example

```kotlin
// Basic usage with title and automatic back button
AppBar(
    title = "Settings"
)

// With custom content
AppBar(
    title = "Profile",
    modifier = Modifier.background(Color(LocalColors.current.surface))
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
        Button(onClick = { /* Save action */ }) {
            Text("Save")
        }
    }
}
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
    AppBar(title = "Button Component Demo")
}
```
