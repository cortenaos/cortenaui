# Text

`Text` is the fundamental typography component in CortenaUI used to render strings and sentences on the screen. It is hardwired directly into the standard typography theme of the Cortena operating system, eliminating the need for developers to depend on the Material Theme.

## Concept

Every text automatically resolves its style based on a semantic role (`TextRole`). This ensures that the appearance of all fonts, text weights, line heights, and colors is uniform and consistent across the operating system without needing to be configured manually on every call.

1. **Default Color**: Automatically resolves against `LocalContentColor.current` or `LocalColors.current.onBackground`.
2. **Default Role**: Defaults to `TextRole.BodyMedium` (14sp) for regular body sentences.
3. **Typography Scaling**: Automatically resolves font sizes, weights, and line heights in SP based on the chosen semantic role.
4. **Weight Override**: An optional `TextWeight` parameter lets a single role render in a heavier weight without switching to a separate role tier. Useful for buttons, tabs, and metadata where the role's size is right but emphasis is required.
5. **Font Family**: Automatically uses `LocalFontFamily.current`, which is provided by `Theme` or `ContentView`. If no custom font is set, the system default font is used. Can be overridden per-instance via the `style` parameter.

## API Reference

```kotlin
@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle? = null,
    role: TextRole? = null,
    weight: TextWeight? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
)
```

### Parameters

| Name       | Data Type      | Description                                                                                                                        |
| ---------- | -------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| `text`     | `String`       | The sentence / text you want to render.                                                                                            |
| `modifier` | `Modifier`     | Standard Compose modifier.                                                                                                         |
| `color`    | `Color`        | Text color. Defaults to `Color.Unspecified`, resolving dynamically against `LocalContentColor` or theme's `onBackground` color.    |
| `style`    | `TextStyle?`   | Custom `TextStyle` to be merged on top of the resolved role style (e.g., custom letter spacing, text decoration). Default: `null`. |
| `role`     | `TextRole?`    | Semantic typography role. `null` falls back to `LocalTextRole.current` (set by sized parents like `Button`), then to `BodyMedium`. |
| `weight`   | `TextWeight?`  | Optional weight override. `null` falls back to `LocalTextWeight.current`, then to `TextWeight.Default` (use the role's weight).    |
| `maxLines` | `Int`          | Maximum number of lines for the text to span before being truncated. Default: `Int.MAX_VALUE`.                                     |
| `overflow` | `TextOverflow` | How visual text overflow should be handled (e.g., `TextOverflow.Ellipsis`, `TextOverflow.Clip`). Default: `TextOverflow.Clip`.     |

### TextRole Reference

The `TextRole` enum defines 12 distinct semantic scales across 4 categories:

- **Display**: `DisplayLarge`, `DisplayMedium`, `DisplaySmall` (For massive, prominent titles)
- **Headline**: `HeadlineLarge`, `HeadlineMedium`, `HeadlineSmall` (For main layout headings)
- **Title**: `TitleLarge`, `TitleMedium`, `TitleSmall` (For section headers and sub-elements)
- **Body**: `BodyLarge`, `BodyMedium`, `BodySmall` (For standard readable paragraphs and caption notes)

There is no separate "Label" tier. For buttons, tabs, menu items, and other interactive labels, pick the size from the role above and pass `weight = TextWeight.Medium`.

### TextWeight Reference

| Weight               | Value | Use case                                                         |
| -------------------- | ----- | ---------------------------------------------------------------- |
| `TextWeight.Default` | —     | Inherit the role's natural weight. The default for body copy.    |
| `TextWeight.Medium`  | 500   | Interactive labels (buttons, tabs, menu items), inline emphasis. |
| `TextWeight.Bold`    | 700   | Strong emphasis. Use sparingly.                                  |

### Examples

#### Using Default Style and Role

```kotlin
Text(text = "Hello CortenaOS!")
```

#### Using Semantic Roles and Custom Colors

```kotlin
Text(
    text = "Large Dashboard Header",
    role = TextRole.DisplayMedium,
    color = Color(LocalColors.current.primary)
)
```

#### Replacing the Old Label Roles

```kotlin
// Old: TextRole.LabelLarge
Text(text = "Save", role = TextRole.BodyMedium, weight = TextWeight.Medium)

// Old: TextRole.LabelSmall
Text(text = "Caption", role = TextRole.BodySmall, weight = TextWeight.Medium)
```

#### Advanced Customizations (Style Merging & Ellipsis Truncation)

```kotlin
Text(
    text = "This is a very long heading that will automatically be truncated with an ellipsis if it exceeds a single line in length.",
    role = TextRole.TitleMedium,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
    style = TextStyle(
        textDecoration = TextDecoration.Underline,
        letterSpacing = 1.5.sp
    )
)
```

#### Overriding Font Family Per-Instance

```kotlin
// All Text components automatically use the font set in ContentView/Theme.
// To override a specific instance, pass fontFamily via the style parameter:
Text(
    text = "Monospaced code snippet",
    role = TextRole.BodyMedium,
    style = TextStyle(fontFamily = FontFamily.Monospace)
)
```
