# ContentView

`ContentView` is an extension function of `ComponentActivity` in the `:compose` Android module. It serves as the _entry point_ where the application's root Compose interface is injected.

## Characteristics

- **Not a standard @Composable**: This function is equivalent to `setContent {}`. Therefore, it is called once during `onCreate()` in the `Activity`.
- **Built-In Edge-to-Edge**: Directly calls `enableEdgeToEdge()` so the content spans across System Bars and Navigation Bars.
- **Auto Theme Wrapping**: This function wraps its content in `Theme` directly to execute Dependency Injection for _Design Tokens_.
- **Status Bar Layering**: It uses the highest Compose z-index to render the `statusBarColor`, ensuring it remains visible even if you render a header (e.g. `AppBar`) that overlaps into the status bar insets.

## API Reference

```kotlin
@OptIn(ExperimentalComponentsApi::class)
fun ComponentActivity.ContentView(
    themeMode: () -> ThemeMode = { ThemeMode.Auto },
    statusBarIconMode: () -> StatusBarIconMode = { StatusBarIconMode.Auto },
    statusBarColor: () -> Color = { Color.Transparent },
    palette: Palette? = null,
    typography: Typography = DefaultTypography,
    fontFamily: FontFamily? = null,
    sizeToken: SizeToken = SizeToken.Medium,
    dynamicColor: Boolean = false,
    appBar: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
)

enum class StatusBarIconMode {
    Light,
    Dark,
    Auto,
}

enum class ThemeMode {
    Light,
    Dark,
    Auto,
}
```

### Parameters

| Name                              | Data Type                                                     | Description                                                                                                                                             |
| --------------------------------- | ------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `themeMode`                       | `(() -> ThemeMode)? = null -> ThemeMode.Auto`                 | Sets the theme (`Light`, `Dark`, or follows the system via `Auto`). Default: `Auto`.                                                                    |
| `statusBarIconMode`               | `(() -> StatusBarIconMode)? = null -> StatusBarIconMode.Auto` | Determines the icon color (battery, clock, signal). There is an `Auto` mode that mathematically calculates the _luminance_ score from `statusBarColor`. |
| `statusBarColor`                  | `(() -> Color)? = null -> Color.Transparent`                  | The color of the status bar. It is recommended to match this with the _AppBar_ color.                                                                   |
| `palette`                         | `Palette?`                                                    | Allows developers to override colors. If `null`, it will automatically follow `themeMode`.                                                              |
| `typography`                      | `(() -> Typography)? = null -> DefaultTypography`             | Override the design's typography scale. Default: `DefaultTypography`.                                                                                   |
| `fontFamily`                      | `FontFamily?`                                                 | Custom font family applied globally to all `Text` components. If `null`, uses the system default font (`FontFamily.Default`).                           |
| `dynamicColor` **(Experimental)** | `Boolean`                                                     | Provides the dynamic color feature (Material You). Not fully implemented yet, currently only gives a warning in Logcat.                                 |
| `appBar`                          | `(@Composable () -> Unit)? = null`                            | A dedicated layout slot for the AppBar. Fills the slot provided in the topmost `Column`.                                                                |
| `content`                         | `@Composable () -> Unit`                                      | The main content slot of your application (e.g., `Body`).                                                                                               |
