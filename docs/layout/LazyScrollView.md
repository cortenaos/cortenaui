# LazyScrollView

`LazyScrollView` is CortenaUI's lazy 1D scrollable container. It composes only the children currently visible in the viewport, making it the right tool for long lists or rows with many items.

## Concept

`LazyScrollView` wraps `LazyColumn` (vertical) or `LazyRow` (horizontal) and adds CortenaUI's bounce overscroll, auto-hiding scroll indicator with drag-to-scrub, threshold auto-release, and motion-system-driven feel. Children are described through Compose's standard [`LazyListScope`](https://developer.android.com/jetpack/androidx/releases/compose-foundation) DSL so you keep `item`, `items`, and `stickyHeader` semantics including item keys and `Modifier.animateItem()`.

Key behaviors:

- **Lazy composition**: items outside the viewport are not composed. Reuse `state.scrollToItem` for jumping to specific positions.
- **Bounce overscroll**: pulled past the bound, content rubber-bands and auto-releases after a threshold.
- **Sticky header support**: use `stickyHeader { ... }` inside the content DSL to pin a row at the leading edge while the rest scrolls.
- **Auto-hide indicator**: fades in on activity, fades out after a short idle period.
- **Drag-to-scrub indicator**: tap and drag the indicator to skim through long lists. The indicator thickens slightly while held.
- **Item animations**: standard `Modifier.animateItem()` works without ceremony.

## API Reference

```kotlin
@Composable
fun LazyScrollView(
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Vertical,
    state: LazyListState = rememberLazyListState(),
    enabled: Boolean = true,
    reverseLayout: Boolean = false,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    showScrollIndicator: Boolean = true,
    indicatorThickness: Dp = 3.dp,
    indicatorColor: Color = Color.Unspecified,
    indicatorShape: Shape = CapsuleShape(),
    indicatorPadding: Dp = 2.dp,
    indicatorPosition: ScrollIndicatorPosition = ScrollIndicatorPosition.End,
    autoHideIndicator: Boolean = true,
    draggableIndicator: Boolean = true,
    onFirstVisibleItemChanged: ((index: Int) -> Unit)? = null,
    onReachedStart: (() -> Unit)? = null,
    onReachedEnd: (() -> Unit)? = null,
    content: LazyListScope.() -> Unit,
)
```

### Parameters

| Name                        | Type                       | Description                                                                     |
| --------------------------- | -------------------------- | ------------------------------------------------------------------------------- |
| `modifier`                  | `Modifier`                 | Standard Compose modifier.                                                      |
| `orientation`               | `Orientation`              | `Vertical` (LazyColumn) or `Horizontal` (LazyRow). Default: `Vertical`.         |
| `state`                     | `LazyListState`            | Hoistable list state. Default: `rememberLazyListState()`.                       |
| `enabled`                   | `Boolean`                  | Enables user scroll input. Default: `true`.                                     |
| `reverseLayout`             | `Boolean`                  | Reverses layout order; useful for chat lists. Default: `false`.                 |
| `flingBehavior`             | `FlingBehavior`            | Fling physics. Default: platform default.                                       |
| `contentPadding`            | `PaddingValues`            | Padding around the lazy content. Default: `0.dp`.                               |
| `verticalArrangement`       | `Arrangement.Vertical`     | Spacing between items in vertical orientation. Default: `Arrangement.Top`.      |
| `horizontalArrangement`     | `Arrangement.Horizontal`   | Spacing between items in horizontal orientation. Default: `Arrangement.Start`.  |
| `verticalAlignment`         | `Alignment.Vertical`       | Cross-axis alignment in horizontal orientation. Default: `Alignment.Top`.       |
| `horizontalAlignment`       | `Alignment.Horizontal`     | Cross-axis alignment in vertical orientation. Default: `Alignment.Start`.       |
| `showScrollIndicator`       | `Boolean`                  | Toggles the scroll indicator. Default: `true`.                                  |
| `indicatorThickness`        | `Dp`                       | Indicator bar thickness. Default: `3.dp`.                                       |
| `indicatorColor`            | `Color`                    | Indicator color. Defaults to `outline` color from theme.                        |
| `indicatorShape`            | `Shape`                    | Indicator shape. Default: `CapsuleShape()`.                                     |
| `indicatorPadding`          | `Dp`                       | Padding around the indicator. Default: `2.dp`.                                  |
| `indicatorPosition`         | `ScrollIndicatorPosition`  | `Start` or `End`. Default: `End`.                                               |
| `autoHideIndicator`         | `Boolean`                  | Fade indicator out after idle. Default: `true`.                                 |
| `draggableIndicator`        | `Boolean`                  | Allow user to drag the indicator to scrub. Default: `true`.                     |
| `onFirstVisibleItemChanged` | `((Int) -> Unit)?`         | Called whenever the first visible item index changes.                           |
| `onReachedStart`            | `(() -> Unit)?`            | Called when the first item enters view.                                         |
| `onReachedEnd`              | `(() -> Unit)?`            | Called when the last item enters view.                                          |
| `content`                   | `LazyListScope.() -> Unit` | Standard `LazyListScope` DSL — `item`, `items`, `itemsIndexed`, `stickyHeader`. |

## Examples

### Long vertical list

```kotlin
LazyScrollView(modifier = Modifier.fillMaxSize()) {
    items(messages, key = { it.id }) { message ->
        MessageRow(message, modifier = Modifier.animateItem())
    }
}
```

### Horizontal carousel of cards

```kotlin
LazyScrollView(
    orientation = Orientation.Horizontal,
    contentPadding = PaddingValues(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
) {
    items(featured) { feature ->
        FeatureCard(feature)
    }
}
```

### Sticky section headers

```kotlin
LazyScrollView {
    items.groupBy { it.category }.forEach { (category, group) ->
        stickyHeader {
            Text(category, role = TextRole.TitleSmall, weight = TextWeight.Medium)
        }
        items(group) { item -> ItemRow(item) }
    }
}
```

### Reach-end pagination

```kotlin
LazyScrollView(
    onReachedEnd = { viewModel.loadNextPage() },
) {
    items(loadedItems, key = { it.id }) { ItemRow(it) }
    if (isLoading) {
        item { LoadingRow() }
    }
}
```

### Scroll programmatically via hoisted state

```kotlin
val listState = rememberLazyListState()
val scope = rememberCoroutineScope()

LazyScrollView(state = listState) {
    items(100) { Text("Item $it") }
}

Button(onClick = { scope.launch { listState.scrollToItem(50) } }) {
    Text("Jump to 50")
}
```
