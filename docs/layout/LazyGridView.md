# LazyGridView

`LazyGridView` is CortenaUI's lazy 2D grid container. It composes only the cells currently visible in the viewport, making it the right tool for large grids — image galleries, product catalogs, icon pickers with hundreds of entries.

## Concept

`LazyGridView` wraps `LazyVerticalGrid` (vertical) or `LazyHorizontalGrid` (horizontal) and adds CortenaUI's bounce overscroll, auto-hiding scroll indicator with drag-to-scrub, threshold auto-release, and motion-system-driven feel. Cells are described through Compose's standard [`LazyGridScope`](https://developer.android.com/jetpack/androidx/releases/compose-foundation) DSL so you keep `item`, `items`, and `Modifier.animateItem()`.

Cell layout follows the same [`GridColumns`](GridView.md#gridcolumns) contract as eager `GridView`:

- **`GridColumns.Fixed(count)`** — fixed cell count per cross-axis line.
- **`GridColumns.Adaptive(minSize)`** — fits as many cells as possible while keeping each at least `minSize` wide.

For vertical orientation, `columns` controls how many cells fit horizontally per row; for horizontal orientation, it controls how many cells fit vertically per column.

Key behaviors:

- **Lazy composition**: only cells in the viewport are composed. Scrolling thousands of items stays smooth.
- **Bounce overscroll**: rubber-band drag and threshold auto-release.
- **Sticky header**: emit `item(span = { GridItemSpan(maxLineSpan) }) { ... }` to span a full line as a header.
- **Auto-hide indicator**: fades on activity, hides after idle.
- **Drag-to-scrub indicator**: tap and drag the indicator to skim large grids.

## API Reference

```kotlin
@Composable
fun LazyGridView(
    columns: GridColumns,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Vertical,
    state: LazyGridState = rememberLazyGridState(),
    enabled: Boolean = true,
    reverseLayout: Boolean = false,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    showScrollIndicator: Boolean = true,
    indicatorThickness: Dp = 3.dp,
    indicatorColor: Color = Color.Unspecified,
    indicatorShape: Shape = CapsuleShape(),
    indicatorPadding: Dp = 2.dp,
    indicatorPosition: ScrollIndicatorPosition = ScrollIndicatorPosition.End,
    autoHideIndicator: Boolean = true,
    draggableIndicator: Boolean = true,
    content: LazyGridScope.() -> Unit,
)
```

### Parameters

| Name                    | Type                       | Description                                                                              |
| ----------------------- | -------------------------- | ---------------------------------------------------------------------------------------- |
| `columns`               | `GridColumns`              | Cross-axis sizing. See [`GridColumns`](GridView.md#gridcolumns).                         |
| `modifier`              | `Modifier`                 | Standard Compose modifier.                                                               |
| `orientation`           | `Orientation`              | `Vertical` (LazyVerticalGrid) or `Horizontal` (LazyHorizontalGrid). Default: `Vertical`. |
| `state`                 | `LazyGridState`            | Hoistable grid state. Default: `rememberLazyGridState()`.                                |
| `enabled`               | `Boolean`                  | Enables user scroll input. Default: `true`.                                              |
| `reverseLayout`         | `Boolean`                  | Reverses cell order along the main axis. Default: `false`.                               |
| `flingBehavior`         | `FlingBehavior`            | Fling physics. Default: platform default.                                                |
| `contentPadding`        | `PaddingValues`            | Padding around the grid. Default: `0.dp`.                                                |
| `verticalArrangement`   | `Arrangement.Vertical`     | Spacing between cells vertically. Default: `Arrangement.Top`.                            |
| `horizontalArrangement` | `Arrangement.Horizontal`   | Spacing between cells horizontally. Default: `Arrangement.Start`.                        |
| `showScrollIndicator`   | `Boolean`                  | Toggles the scroll indicator. Default: `true`.                                           |
| `indicatorThickness`    | `Dp`                       | Indicator bar thickness. Default: `3.dp`.                                                |
| `indicatorColor`        | `Color`                    | Indicator color. Defaults to `outline` color from theme.                                 |
| `indicatorShape`        | `Shape`                    | Indicator shape. Default: `CapsuleShape()`.                                              |
| `indicatorPadding`      | `Dp`                       | Padding around the indicator. Default: `2.dp`.                                           |
| `indicatorPosition`     | `ScrollIndicatorPosition`  | `Start` or `End`. Default: `End`.                                                        |
| `autoHideIndicator`     | `Boolean`                  | Fade indicator out after idle. Default: `true`.                                          |
| `draggableIndicator`    | `Boolean`                  | Allow user to drag the indicator to scrub. Default: `true`.                              |
| `content`               | `LazyGridScope.() -> Unit` | Standard `LazyGridScope` DSL — `item`, `items`, `itemsIndexed`.                          |

## Examples

### Adaptive image grid

```kotlin
LazyGridView(
    columns = GridColumns.Adaptive(minSize = 120.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = PaddingValues(8.dp),
) {
    items(photos, key = { it.id }) { photo ->
        AsyncImage(model = photo.url, modifier = Modifier.fillMaxWidth().aspectRatio(1f))
    }
}
```

### Fixed 4-column icon picker

```kotlin
LazyGridView(
    columns = GridColumns.Fixed(4),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
) {
    items(allIcons) { icon ->
        Icon(imageVector = icon, contentDescription = null)
    }
}
```

### Section header that spans the full line

```kotlin
LazyGridView(columns = GridColumns.Fixed(3)) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Text("Section A", role = TextRole.TitleMedium)
    }
    items(sectionAItems) { ItemTile(it) }

    item(span = { GridItemSpan(maxLineSpan) }) {
        Text("Section B", role = TextRole.TitleMedium)
    }
    items(sectionBItems) { ItemTile(it) }
}
```

### Programmatic scroll via hoisted state

```kotlin
val gridState = rememberLazyGridState()
val scope = rememberCoroutineScope()

LazyGridView(
    columns = GridColumns.Adaptive(minSize = 100.dp),
    state = gridState,
) {
    items(catalog, key = { it.id }) { ProductCard(it) }
}

Button(onClick = { scope.launch { gridState.scrollToItem(0) } }) {
    Text("Back to top")
}
```
