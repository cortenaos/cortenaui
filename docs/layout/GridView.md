# GridView

`GridView` is CortenaUI's eager 2D grid container. Every cell is composed upfront, which makes it the right choice for small grids where the cell count is known and recycling overhead would only get in the way (settings dashboards, color pickers, fixed icon grids). For larger grids that benefit from lazy composition, use [LazyGridView](LazyGridView.md).

## Concept

`GridView` builds a grid by chunking your `items` list into rows (vertical orientation) or columns (horizontal orientation), placing each chunk inside a single `Row` or `Column`. The number of cells per cross-axis line is driven by [`GridColumns`](#gridcolumns):

- **`GridColumns.Fixed(count)`** — fixed cell count. Cells share the cross-axis evenly.
- **`GridColumns.Adaptive(minSize)`** — fits as many cells as possible while keeping each at least `minSize` wide. Recomputed whenever the available cross-axis size changes.

Because every cell is composed upfront, GridView is best for grids in the dozens of cells, not thousands.

Inherits from `ScrollView`:

- Bounce overscroll with rubber-band drag and threshold auto-release.
- Auto-hiding scroll indicator with drag-to-scrub.
- Top / bottom callback hooks.

## API Reference

```kotlin
@Composable
fun <T> GridView(
    items: List<T>,
    columns: GridColumns,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Vertical,
    scrollState: ScrollState = rememberScrollState(),
    enabled: Boolean = true,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    horizontalSpacing: Dp = 0.dp,
    verticalSpacing: Dp = 0.dp,
    showScrollIndicator: Boolean = true,
    indicatorThickness: Dp = 3.dp,
    indicatorColor: Color = Color.Unspecified,
    indicatorShape: Shape = CapsuleShape(),
    indicatorPadding: Dp = 2.dp,
    indicatorPosition: ScrollIndicatorPosition = ScrollIndicatorPosition.End,
    autoHideIndicator: Boolean = true,
    draggableIndicator: Boolean = true,
    onScrolled: ((Int, Int) -> Unit)? = null,
    onReachedTop: (() -> Unit)? = null,
    onReachedBottom: (() -> Unit)? = null,
    itemContent: @Composable (index: Int, item: T) -> Unit,
)
```

### Parameters

| Name                  | Type                           | Description                                                                  |
| --------------------- | ------------------------------ | ---------------------------------------------------------------------------- |
| `items`               | `List<T>`                      | Cells to render. Order is row-major (vertical) or column-major (horizontal). |
| `columns`             | `GridColumns`                  | Cross-axis sizing strategy. See [`GridColumns`](#gridcolumns).               |
| `modifier`            | `Modifier`                     | Standard Compose modifier.                                                   |
| `orientation`         | `Orientation`                  | `Vertical` or `Horizontal`. Default: `Vertical`.                             |
| `scrollState`         | `ScrollState`                  | Hoistable scroll state. Default: `rememberScrollState()`.                    |
| `enabled`             | `Boolean`                      | Enables user scroll input. Default: `true`.                                  |
| `flingBehavior`       | `FlingBehavior`                | Fling physics. Default: platform default.                                    |
| `contentPadding`      | `PaddingValues`                | Padding around the grid. Default: `0.dp`.                                    |
| `horizontalSpacing`   | `Dp`                           | Spacing between cells along the horizontal axis. Default: `0.dp`.            |
| `verticalSpacing`     | `Dp`                           | Spacing between cells along the vertical axis. Default: `0.dp`.              |
| `showScrollIndicator` | `Boolean`                      | Toggles the scroll indicator. Default: `true`.                               |
| `indicatorThickness`  | `Dp`                           | Indicator bar thickness. Default: `3.dp`.                                    |
| `indicatorColor`      | `Color`                        | Indicator color. Defaults to `outline` color from theme.                     |
| `indicatorShape`      | `Shape`                        | Indicator shape. Default: `CapsuleShape()`.                                  |
| `indicatorPadding`    | `Dp`                           | Padding around the indicator. Default: `2.dp`.                               |
| `indicatorPosition`   | `ScrollIndicatorPosition`      | `Start` or `End`. Default: `End`.                                            |
| `autoHideIndicator`   | `Boolean`                      | Fade indicator out after idle. Default: `true`.                              |
| `draggableIndicator`  | `Boolean`                      | Allow user to drag the indicator to scrub. Default: `true`.                  |
| `onScrolled`          | `((Int, Int) -> Unit)?`        | Called with `(scrollValue, maxScrollValue)` on every scroll change.          |
| `onReachedTop`        | `(() -> Unit)?`                | Called when scroll reaches `0`.                                              |
| `onReachedBottom`     | `(() -> Unit)?`                | Called when scroll reaches `maxValue`.                                       |
| `itemContent`         | `@Composable (Int, T) -> Unit` | Renders one cell. Receives the flat index and the item.                      |

### `GridColumns`

```kotlin
sealed class GridColumns {
    data class Fixed(val count: Int) : GridColumns()
    data class Adaptive(val minSize: Dp) : GridColumns()
}
```

| Variant                     | Behavior                                                                               |
| --------------------------- | -------------------------------------------------------------------------------------- |
| `GridColumns.Fixed(count)`  | Always renders exactly `count` cells per cross-axis line. Cells share the axis evenly. |
| `GridColumns.Adaptive(min)` | Computes the cell count from the cross-axis size so each cell is at least `min` wide.  |

## Examples

### Fixed 3-column grid

```kotlin
GridView(
    items = (1..12).toList(),
    columns = GridColumns.Fixed(3),
    horizontalSpacing = 8.dp,
    verticalSpacing = 8.dp,
    contentPadding = PaddingValues(8.dp),
) { _, value ->
    Box(
        modifier = Modifier.fillMaxWidth().height(56.dp).background(Color.Gray),
        contentAlignment = Alignment.Center,
    ) {
        Text("$value")
    }
}
```

### Adaptive responsive grid

```kotlin
GridView(
    items = palette,
    columns = GridColumns.Adaptive(minSize = 96.dp),
    horizontalSpacing = 12.dp,
    verticalSpacing = 12.dp,
) { _, swatch ->
    SwatchTile(swatch)
}
```

The number of columns recomputes automatically as the container's width changes — perfect for screens that may render on phones, tablets, or foldables.

### Horizontal grid (rows instead of columns)

```kotlin
GridView(
    items = quickActions,
    columns = GridColumns.Fixed(2),
    orientation = Orientation.Horizontal,
    horizontalSpacing = 8.dp,
    verticalSpacing = 8.dp,
) { _, action ->
    QuickActionTile(action)
}
```

In horizontal orientation, `GridColumns` controls **rows** instead of columns, and the grid scrolls horizontally.
