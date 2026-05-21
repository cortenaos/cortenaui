# ScrollView

`ScrollView` is Cortena's scrollable container with bounce overscroll and a smart scroll indicator that auto-hides, animates, and supports direct dragging.

!!! warning "Known Limitation With ScrollView"

    Currently, if you wish to use `SafeArea` & `ScrollView` composables together. you need to wrapper the SafeView inside ScrollView,
    otherwise thier are very wired quirks with the placement of scrollbars.

    We know its unintuative and are currently trying to fix it.

    ```kotlin
    ScrollView {
        SafeArea {
            ...
        }
    }
    ```

## Concept

`ScrollView` wraps content in a scrollable region that supports both vertical and horizontal orientations. When the user scrolls past the edges, the content bounces with a spring-back animation instead of the default Android glow/stretch effect. Pulling the overscroll past a threshold while still holding triggers an auto-release that springs the content back on its own.

A thin capsule-shaped scroll indicator appears automatically when content overflows, proportionally sized to the viewport-to-content ratio. It tracks the scroll position 1:1 for a fully responsive feel, fades out shortly after scrolling stops, and the user can grab it directly to scrub through long content.

Key behaviors:

- **Bounce overscroll**: content shifts with rubber-band resistance during drag, then springs back on release. Auto-releases when pulled past the threshold.
- **Auto-hide**: the indicator fades out after the user stops interacting and reappears immediately on the next scroll or drag.
- **Draggable indicator**: tap and drag the indicator to scrub through content. Drag tracks the finger 1:1 so scrubbing feels precise. The indicator visibly thickens while held to confirm the active interaction.
- **Callbacks**: optional listeners for scroll position, top-reached, and bottom-reached events.

## API Reference

```kotlin
@Composable
fun ScrollView(
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Vertical,
    scrollState: ScrollState = rememberScrollState(),
    enabled: Boolean = true,
    reverseLayout: Boolean = false,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    showScrollIndicator: Boolean = true,
    indicatorThickness: Dp = 3.dp,
    indicatorColor: Color = Color.Unspecified,
    indicatorShape: Shape = CapsuleShape(),
    indicatorPadding: Dp = 2.dp,
    indicatorPosition: ScrollIndicatorPosition = ScrollIndicatorPosition.End,
    autoHideIndicator: Boolean = true,
    draggableIndicator: Boolean = true,
    onScrolled: ((scrollValue: Int, maxScrollValue: Int) -> Unit)? = null,
    onReachedTop: (() -> Unit)? = null,
    onReachedBottom: (() -> Unit)? = null,
    content: @Composable () -> Unit,
)

enum class ScrollIndicatorPosition {
    Start,
    End
}
```

### Parameters

| Name                  | Data Type                 | Description                                                                              |
| --------------------- | ------------------------- | ---------------------------------------------------------------------------------------- |
| `modifier`            | `Modifier`                | Standard Compose modifier.                                                               |
| `orientation`         | `Orientation`             | Scroll direction. Default: `Orientation.Vertical`.                                       |
| `scrollState`         | `ScrollState`             | Externally hoisted scroll state. Default: `rememberScrollState()`.                       |
| `enabled`             | `Boolean`                 | Enables or disables scrolling. Default: `true`.                                          |
| `reverseLayout`       | `Boolean`                 | Reverses the scroll direction. Default: `false`.                                         |
| `flingBehavior`       | `FlingBehavior`           | Fling physics. Default: platform default via `ScrollableDefaults`.                       |
| `contentPadding`      | `PaddingValues`           | Inner padding applied to the scrollable content. Default: `0.dp`.                        |
| `showScrollIndicator` | `Boolean`                 | Shows/hides the scroll indicator. Default: `true`.                                       |
| `indicatorThickness`  | `Dp`                      | Thickness of the indicator bar. Default: `3.dp`.                                         |
| `indicatorColor`      | `Color`                   | Color of the indicator. Default: `outline` token from `LocalColors`.                     |
| `indicatorShape`      | `Shape`                   | Shape of the indicator. Default: `CapsuleShape()`.                                       |
| `indicatorPadding`    | `Dp`                      | Padding around the indicator. Default: `2.dp`.                                           |
| `indicatorPosition`   | `ScrollIndicatorPosition` | Indicator placement (`Start` or `End`). Default: `End`.                                  |
| `autoHideIndicator`   | `Boolean`                 | When `true`, the indicator fades out after a short idle period. Default: `true`.         |
| `draggableIndicator`  | `Boolean`                 | When `true`, the indicator can be grabbed and dragged to scrub content. Default: `true`. |
| `onScrolled`          | `((Int, Int) -> Unit)?`   | Called with `(scrollValue, maxScrollValue)` on every scroll change.                      |
| `onReachedTop`        | `(() -> Unit)?`           | Called when scroll position reaches the top (value == 0).                                |
| `onReachedBottom`     | `(() -> Unit)?`           | Called when scroll position reaches the bottom (value == maxValue).                      |
| `content`             | `@Composable () -> Unit`  | The scrollable content.                                                                  |

### Example

Basic vertical scroll:

```kotlin
ScrollView(modifier = Modifier.fillMaxSize()) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        repeat(50) { index ->
            Text("Item $index")
        }
    }
}
```

### Horizontal Scroll

```kotlin
ScrollView(
    orientation = Orientation.Horizontal,
    modifier = Modifier.fillMaxWidth(),
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(20) { index ->
            Box(modifier = Modifier.size(80.dp).background(Color.Gray))
        }
    }
}
```

### Scroll Callbacks

```kotlin
ScrollView(
    modifier = Modifier.fillMaxSize(),
    onScrolled = { value, max -> println("$value / $max") },
    onReachedTop = { println("Reached top") },
    onReachedBottom = { println("Reached bottom") },
) {
    Column {
        repeat(100) { Text("Item $it") }
    }
}
```

### Custom Indicator

```kotlin
ScrollView(
    modifier = Modifier.fillMaxSize(),
    indicatorColor = Color(LocalColors.current.primary),
    indicatorThickness = 4.dp,
    indicatorPosition = ScrollIndicatorPosition.Start,
) {
    Column {
        repeat(50) { Text("Item $it") }
    }
}
```

### Always-Visible Indicator

Disable auto-hide if you want a persistent indicator (for example, a debug screen or a panel where the indicator doubles as a visual scale):

```kotlin
ScrollView(
    modifier = Modifier.fillMaxSize(),
    autoHideIndicator = false,
) {
    Column { repeat(50) { Text("Item $it") } }
}
```

### Disable Drag-to-Scrub

If you do not want the indicator to be interactive (for example, on a read-only preview), turn dragging off:

```kotlin
ScrollView(
    modifier = Modifier.fillMaxSize(),
    draggableIndicator = false,
) {
    Column { repeat(50) { Text("Item $it") } }
}
```
