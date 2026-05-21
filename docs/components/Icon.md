# Icon

`Icon` renders a vector graphic with a tinted color. Built on top of `compose.foundation.Image` so it stays inside the Material-free constraint of `:compose`.

## Concept

An icon in CortenaUI is rendered from an `ImageVector` (typically sourced from `androidx.compose.material.icons` in the consumer app, or any vector asset). It is tinted using the resolved content color and sized via `LocalIconSize`, which lets sized parents like `Button` automatically scale icons with their tier.

1. **Default Size**: Resolves from `LocalIconSize.current`. Inside a `Button`, this matches the button's `SizeToken.iconSize`. Outside any sized scope it defaults to `24.dp`.
2. **Tint Resolution**: Mirrors `Text`. If `tint` is unspecified, falls back to `LocalContentColor.current` and finally to `Palette.onBackground`.
3. **Disabled State**: Renders at `alpha = 0.38f`.
4. **Material-Free**: Does not depend on `androidx.compose.material` or `material3`. Consumers may still source vector assets from Material Icons because `ImageVector` itself lives in `compose.ui.graphics.vector`.

## API Reference

```kotlin
@Composable
fun Icon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = Dp.Unspecified,
    enabled: Boolean = true,
)
```

### Parameters

| Name                 | Data Type     | Description                                                                                                                          |
| -------------------- | ------------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| `imageVector`        | `ImageVector` | The vector graphic to render. Typically from `androidx.compose.material.icons.Icons.Default.*` or a custom asset.                    |
| `contentDescription` | `String?`     | Accessibility description. Pass `null` only for purely decorative icons that have an adjacent text label.                            |
| `modifier`           | `Modifier`    | Standard Compose modifier. Note: size is applied internally based on `size` / `LocalIconSize`; further size modifiers will compound. |
| `tint`               | `Color`       | Tint applied to the vector. Defaults to `Color.Unspecified`, which falls back to `LocalContentColor` or `Palette.onBackground`.      |
| `size`               | `Dp`          | Override the resolved icon size. `Dp.Unspecified` uses `LocalIconSize.current`.                                                      |
| `enabled`            | `Boolean`     | When `false` renders at `alpha = 0.38f`. Default: `true`.                                                                            |

## Examples

#### Inside a Button — automatic sizing

```kotlin
Button(onClick = { }, size = SizeToken.Large) {
    Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorite")
    Text("Like")
}
// The icon renders at the Large tier's iconSize (≈25.4 dp); the Text renders at BodyLarge.
```

#### Standalone with a custom tint

```kotlin
Icon(
    imageVector = Icons.Default.Add,
    contentDescription = "Add",
    tint = Color(LocalColors.current.primary),
    size = 28.dp,
)
```

#### Decorative icon next to a label

```kotlin
Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(imageVector = Icons.Default.Info, contentDescription = null)
    Text(" 12 unread", role = TextRole.BodySmall)
}
```
