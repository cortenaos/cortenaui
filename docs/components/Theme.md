# Theme

`Theme` is the _Root Theming Container_ of the Cortena Components design system. It is the heart that maps the _ThemeMode_ (Dark/Light/Auto) and utilizes the _CompositionLocalProvider_ from Compose to distribute all UI properties to all child composables that use it.

## Concept

Any component written to Cortena specifications, such as `Text` or `SafeArea`, reads its color, spacing, motion, or shape properties via:

- `LocalIsDark.current`
- `LocalColors.current`
- `LocalTypography.current`
- `LocalFontFamily.current`
- `LocalSpacing.current`
- `LocalSizeToken.current`
- `LocalMotion.current`

The function of `Theme` is to provide these actual values at the top of the hierarchy (root node). `ContentView` already calls this internally, so you rarely need to call it directly unless you are inside a preview function (`@Preview`) of pure compose.

## API Reference

```kotlin
@Composable
fun Theme(
    themeMode: ThemeMode = ThemeMode.Auto,
    palette: Palette? = null,
    typography: Typography = DefaultTypography,
    fontFamily: FontFamily? = null,
    sizeToken: SizeToken = SizeToken.Medium,
    motion: Motion = DefaultMotion,
    content: @Composable () -> Unit
)
```

### Parameters

| Name         | Data Type                | Description                                                                                                                              |
| ------------ | ------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------- |
| `themeMode`  | `ThemeMode`              | Options are `ThemeMode.Light`, `ThemeMode.Dark`, or `ThemeMode.Auto` (detects current OS mode).                                          |
| `palette`    | `Palette?`               | Ignores automatic OS detection and forces the specific palette you want. If `null`, it will automatically resolve following `themeMode`. |
| `typography` | `Typography`             | Overrides the adjusted _font scale_. Default: `DefaultTypography`.                                                                       |
| `fontFamily` | `FontFamily?`            | Custom font family applied to all `Text` components. If `null`, uses the system default font (`FontFamily.Default`).                     |
| `sizeToken`  | `SizeToken`              | Sets the global component size tier. All sized components inherit this. Default: `SizeToken.Medium`.                                     |
| `motion`     | `Motion`                 | Spring presets, durations, and easings consumed by every interactive component. Default: `DefaultMotion`. See [Motion](Motion.md).       |
| `content`    | `@Composable () -> Unit` | Lambda for your child Composable function placed under the umbrella of this theme.                                                       |
