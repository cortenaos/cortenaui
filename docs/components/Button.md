# Button

`Button` is Cortena's primary action component.

## Concept

`Button` renders a capsule-shaped surface with an automatic tonal press highlight and damped drag response.

Styles and variants are driven by `LocalColors.current`:

- `Primary`: `primary` background, `onPrimary` content.
- `Secondary`: `secondary` background, `onSecondary` content.
- `Accent`: `accent` background, `onAccent` content.
- `Ghost`: `secondary @ 20%` background, `onSurfaceVariant` content.
- `Destructive`: `error` background, `onError` content.

Each style supports a `Soft` variant that reduces the background opacity, making the foreground color the visual focus:

- `Default`: filled background.
- `Soft`: low-opacity background (`8–12%`), style color as foreground.

### Content Scaling

The `Button` content slot automatically scales with the button's `size` tier. Inside the content lambda:

- Nested `Text` picks up `BodySmall` / `BodyMedium` / `BodyLarge` for `Small` / `Medium` / `Large` respectively, with `weight = TextWeight.Medium`.
- Nested `Icon` picks up the tier's icon size from `LocalIconSize`.

Both can still be overridden by passing `role`, `weight`, or `size` directly to the nested composable.

## API Reference

```kotlin
@Composable
fun Button(
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    iconOnly: Boolean = false,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactive: Boolean = true,
    size: SizeToken = LocalSizeToken.current,
    style: ButtonStyle = ButtonStyle.Primary,
    variant: ButtonVariant = ButtonVariant.Default,
    background: Color = Color.Unspecified,
    foreground: Color = Color.Unspecified,
    content: @Composable RowScope.() -> Unit,
)

enum class ButtonStyle {
    Primary,
    Secondary,
    Accent,
    Ghost,
    Destructive,
}

enum class ButtonVariant {
    Default,
    Soft,
}
```

### Parameters

| Name          | Type                              | Default                  | Description                                                                                                    |
| ------------- | --------------------------------- | ------------------------ | -------------------------------------------------------------------------------------------------------------- |
| `onClick`     | `(() -> Unit)?`                   | `null`                   | Called when the button is clicked.                                                                             |
| `onLongClick` | `(() -> Unit)?`                   | `null`                   | Called when the button is long-clicked.                                                                        |
| `iconOnly`    | `Boolean`                         | `false`                  | If `true`, applies specific sizing and padding to make the button perfectly round/square for icons.            |
| `modifier`    | `Modifier`                        | `Modifier`               | Standard Compose modifier.                                                                                     |
| `enabled`     | `Boolean`                         | `true`                   | Disables interaction and renders the button at 38% opacity when `false`.                                       |
| `interactive` | `Boolean`                         | `true`                   | Enables press scale, drag offset, and directional stretch animations when `true`.                              |
| `size`        | `SizeToken`                       | `LocalSizeToken.current` | Controls the button height, horizontal padding, icon-only width, and content gap.                              |
| `style`       | `ButtonStyle`                     | `ButtonStyle.Primary`    | Determines the color role used for background and content.                                                     |
| `variant`     | `ButtonVariant`                   | `ButtonVariant.Default`  | `Default` renders a filled background. `Soft` renders a low-opacity background with style color as foreground. |
| `background`  | `Color`                           | `Color.Unspecified`      | Overrides the style background color when specified.                                                           |
| `foreground`  | `Color`                           | `Color.Unspecified`      | Overrides the style foreground color when specified.                                                           |
| `content`     | `@Composable RowScope.() -> Unit` | —                        | Row content rendered inside the button.                                                                        |

### Style

| Style         | Default Background | Default Foreground | Soft Background   | Soft Foreground    |
| ------------- | ------------------ | ------------------ | ----------------- | ------------------ |
| `Primary`     | `primary`          | `onPrimary`        | `primary @ 12%`   | `primary`          |
| `Secondary`   | `secondary`        | `onSecondary`      | `secondary @ 12%` | `secondary`        |
| `Accent`      | `accent`           | `onAccent`         | `accent @ 12%`    | `accent`           |
| `Ghost`       | `secondary @ 20%`  | `onSurfaceVariant` | `secondary @ 8%`  | `onSurfaceVariant` |
| `Destructive` | `error`            | `onError`          | `secondary @ 12%` | `error`            |

## Examples

```kotlin
// Basic usage
Button(onClick = { }) {
    Text("Submit")
}

// Accent style
Button(
    onClick = { },
    style = ButtonStyle.Accent,
) {
    Text("Featured")
}

// Soft variant
Button(
    onClick = { },
    style = ButtonStyle.Destructive,
    variant = ButtonVariant.Soft,
) {
    Text("Delete")
}

// Disabled
Button(
    onClick = { },
    style = ButtonStyle.Primary,
    enabled = false,
) {
    Text("Unavailable")
}

// Custom color
Button(
    onClick = { },
    background = Color(0xFFFF9500),
    foreground = Color.White,
) {
    Text("Orange")
}

// With long click
Button(
    onClick = { },
    onLongClick = { },
    style = ButtonStyle.Secondary,
) {
    Icon(Icons.Default.Favorite, contentDescription = null)
    Text("Favorite")
}

// Small size
Button(
    onClick = { },
    size = SizeToken.Small,
) {
    Text("Compact")
}

// Large size
Button(
    onClick = { },
    size = SizeToken.Large,
) {
    Text("Prominent")
}
```
