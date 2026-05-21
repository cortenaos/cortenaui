# Body

`Body` is the primary surface canvas container of your Cortena application. You use this layout to place all your content (like lists, cards, articles) below the status bar / in the main body area of the screen.

## Concept

- `Body` automatically uses `Modifier.fillMaxSize()` to consume the maximum remaining space from its parent (like the `Column` inside `ContentView`).
- Ideally, `Body` is given the primary background of the application (e.g., `Modifier.background(Color(LocalColors.current.background))`).

## API Reference

```kotlin
@Composable
fun Body(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
)
```

### Parameters

| Name       | Data Type                | Description                                                                             |
| ---------- | ------------------------ | --------------------------------------------------------------------------------------- |
| `modifier` | `Modifier`               | Standard Compose modifier used to stack actions like _background_, root _padding_, etc. |
| `content`  | `@Composable () -> Unit` | The child Composable elements contained inside the body.                                |

### Example

```kotlin
Body(
    modifier = Modifier.background(Color(LocalColors.current.background))
) {
    // Write your layout here
    SafeArea {
        Text("Message List")
    }
}
```
