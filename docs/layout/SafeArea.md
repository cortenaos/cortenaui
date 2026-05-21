# SafeArea

`SafeArea` is a basic static layout useful for providing "safe" margin distances from the edges of the screen to the content, to align with Cortena's design specifications (such as avoiding collisions with curved screen boundaries).

## Concept

According to Cortena's _Design Guidelines_, every text content and component should not be placed 100% attached to the screen edges (left-right). The mandatory/standard distance is the value of `Spacing.Md` (around 16dp).
Instead of literally/manually adding `padding(16.dp)` to every element (which means hardcoding the screen layout), use the `SafeArea` wrapper.

## API Reference

```kotlin
@Composable
fun SafeArea(
    modifier: Modifier = Modifier,
    horizontal: Dp = LocalSpacing.current.Md.dp,
    vertical: Dp = LocalSpacing.current.None.dp,
    content: @Composable () -> Unit,
)
```

### Parameters

| Name         | Data Type                | Description                                                                                          |
| ------------ | ------------------------ | ---------------------------------------------------------------------------------------------------- |
| `modifier`   | `Modifier`               | Standard Compose modifier.                                                                           |
| `horizontal` | `Dp`                     | Padding width (left-right). Defaults to using the design token variable `Md.dp` from `LocalSpacing`. |
| `vertical`   | `Dp`                     | Padding width (top-bottom). Defaults to `None.dp` (0 dp).                                            |
| `content`    | `@Composable () -> Unit` | The main component inside the box.                                                                   |

### Usage

It is always recommended to use this to wrap columns and rows.

```kotlin
SafeArea {
    Column {
        Text("Login Form")
        // ... Input Fields
    }
}
```
