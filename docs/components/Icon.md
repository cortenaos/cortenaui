# Icon

`Icon` renders a graphic icon with a tinted color. It provides two overloads: one for `IconRenderer` lambdas (used by font-backed icon packs like Phosphor Icons) and one for `ImageVector` assets. Built on top of `compose.foundation.Image` and `BasicText` so it stays inside the Material-free constraint of `:compose`.

## Concept

An icon in CortenaUI relies on CortenaUI to resolve its size and tint, while delegating the actual drawing to a renderer or vector painter.

1. **Default Size**: Resolves from `LocalIconSize.current`. Inside a `Button`, this matches the button's `SizeToken.iconSize`. Outside any sized scope it defaults to `24.dp`.
2. **Tint Resolution**: Mirrors `Text`. If `tint` is unspecified, falls back to `LocalContentColor.current` and finally to `Palette.onBackground`.
3. **Disabled State**: Renders at `alpha = 0.38f`.
4. **Renderer vs Vector**: You can use CortenaUI's official `cortenaui-phosphor-icons` library which provides an `IconRenderer`, or fallback to custom `ImageVector` assets.

## API Reference

### Renderer Overload (Recommended)

Used for external icon packs like Phosphor Icons that implement the `IconRenderer` contract.

```kotlin
@Composable
fun Icon(
    renderer: IconRenderer,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = Dp.Unspecified,
    enabled: Boolean = true,
)
```

### ImageVector Overload

Used for custom vector assets or legacy `androidx.compose.material.icons` graphics.

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

| Name                 | Data Type      | Description                                                                                                                          |
| -------------------- | -------------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| `renderer`           | `IconRenderer` | A composable lambda that draws the icon glyph. Typically provided by `PhosphorIcon(...)`.                                            |
| `imageVector`        | `ImageVector`  | The vector graphic to render. Typically from a custom asset.                                                                         |
| `contentDescription` | `String?`      | Accessibility description. Pass `null` only for purely decorative icons that have an adjacent text label.                            |
| `modifier`           | `Modifier`     | Standard Compose modifier. Note: size is applied internally based on `size` / `LocalIconSize`; further size modifiers will compound. |
| `tint`               | `Color`        | Tint applied to the graphic. Defaults to `Color.Unspecified`, which falls back to `LocalContentColor` or `Palette.onBackground`.     |
| `size`               | `Dp`           | Override the resolved icon size. `Dp.Unspecified` uses `LocalIconSize.current`.                                                      |
| `enabled`            | `Boolean`      | When `false` renders at `alpha = 0.38f`. Default: `true`.                                                                            |

## Examples

### Inside a Button — automatic sizing

```kotlin
Button(onClick = { }, size = SizeToken.Large) {
    Icon(
        renderer = PhosphorIcon(PhosphorIcons.Fill.Heart),
        contentDescription = "Favorite",
    )
    Text("Like")
}
// The icon renders at the Large tier's iconSize (≈25.4 dp); the Text renders at BodyLarge.
```

### Standalone with a custom tint

```kotlin
Icon(
    renderer = PhosphorIcon(PhosphorIcons.Regular.Plus),
    contentDescription = "Add",
    tint = Color(LocalColors.current.primary),
    size = 28.dp,
)
```

### Decorative icon next to a label

```kotlin
Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(
        renderer = PhosphorIcon(PhosphorIcons.Regular.Info),
        contentDescription = null,
    )
    Text(" 12 unread", role = TextRole.BodySmall)
}
```
