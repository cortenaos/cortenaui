# Shape

CortenaUI's shape system: a sealed `ComponentShape` hierarchy plus a framework-agnostic
continuous-curvature ("squircle") path emitter.

## Concept

The shape system is split across two modules:

- **`:foundation`** holds the pure-Kotlin math: `CornerStyle`, the `CornerBuilder` squircle
  solver, and the `ContinuousCurvature.emit(...)` API that streams path commands into a
  consumer-supplied [`CornerPathReceiver`](#cornerpathreceiver). No graphics framework imports.
- **`:shape`** holds the Compose binding: the sealed `ComponentShape` hierarchy
  (`CapsuleShape`, `RoundedShape`, `UnevenShape`, `RectangleShape`), shape lerp / copy helpers,
  and the bridge that turns the foundation math into `Outline.Generic` for Compose.

Every CortenaUI component renders through `ComponentShape`. Non-Compose consumers (Android View,
Canvas, system surfaces, AOSP) can use the foundation API directly to obtain the same squircle
geometry without taking a Compose dependency.

### Corner styles

`CornerStyle.Circular` uses constant-radius circular arcs at each corner — the standard rounded
rectangle. `CornerStyle.Continuous` uses the continuous-curvature solver from `CornerBuilder`,
producing the smooth iOS-style squircle without the curvature discontinuity where an arc meets a
straight edge. The solver is cached so repeated requests for the same corner topology cost zero
allocation.

### Layout direction

`UnevenShape` and `ComponentShape.copy(topStart = ..., topEnd = ..., ...)` interpret radii in
start/end semantics. The shape system mirrors them automatically under
`LayoutDirection.Rtl`. `ComponentShape.corners(...)` returns the resolved absolute (visual)
radii so renderers do not re-implement the mirror logic.

### Outline selection

The Compose layer picks the cheapest representation for each `createOutline(...)` call:

- `Outline.Rectangle` when all four radii are zero.
- `Outline.Rounded` when the style is `Circular`, or when a square fully reaches the capsule
  limit (where every corner radius is at least `minDimension / 2`).
- `Outline.Generic` backed by `ContinuousCurvature` for the squircle path otherwise.

## API Reference

### `ComponentShape` (`:shape`)

```kotlin
@Immutable
sealed interface ComponentShape : Shape {
    val style: CornerStyle?
    fun corners(size: Size, layoutDirection: LayoutDirection, density: Density): Corners
    fun copy(style: CornerStyle): ComponentShape

    @Immutable
    data class Corners(
        val topLeft: Float,
        val topRight: Float,
        val bottomRight: Float,
        val bottomLeft: Float,
    )
}
```

### Shape implementations (`:shape`)

```kotlin
data object RectangleShape : ComponentShape

class CapsuleShape(style: CornerStyle = CornerStyle.Continuous) : ComponentShape

class RoundedShape(
    cornerRadius: Dp,
    style: CornerStyle = CornerStyle.Continuous,
) : ComponentShape

class UnevenShape(
    cornerRadii: CornerRadii,
    style: CornerStyle = CornerStyle.Continuous,
) : ComponentShape {
    constructor(
        topStart: Dp = 0f.dp,
        topEnd: Dp = 0f.dp,
        bottomEnd: Dp = 0f.dp,
        bottomStart: Dp = 0f.dp,
        style: CornerStyle = CornerStyle.Continuous,
    )
}

@Immutable
data class CornerRadii(val topStart: Dp, val topEnd: Dp, val bottomEnd: Dp, val bottomStart: Dp)
```

### Shape helpers (`:shape`)

```kotlin
fun ComponentShape.copy(cornerRadius: Dp, style: CornerStyle = ...): RoundedShape
fun ComponentShape.copy(cornerRadii: CornerRadii, style: CornerStyle = ...): UnevenShape
fun ComponentShape.copy(
    topStart: Dp = Dp.Unspecified,
    topEnd: Dp = Dp.Unspecified,
    bottomEnd: Dp = Dp.Unspecified,
    bottomStart: Dp = Dp.Unspecified,
    style: CornerStyle = ...,
): ComponentShape

fun lerp(start: ComponentShape, stop: ComponentShape, fraction: Float): ComponentShape
fun lerp(
    start: ComponentShape,
    stop: ComponentShape,
    fraction: Float,
    style: CornerStyle,
): ComponentShape

fun lerp(start: CornerRadii, stop: CornerRadii, fraction: Float): CornerRadii
```

### `CornerStyle` (`:foundation`)

```kotlin
enum class CornerStyle { Circular, Continuous }
```

| Value        | Description                                                                  |
| ------------ | ---------------------------------------------------------------------------- |
| `Circular`   | Constant-radius circular arcs at each corner. Default for legacy aesthetics. |
| `Continuous` | Continuous-curvature squircle. The default for every `ComponentShape` below. |

### `ContinuousCurvature` (`:foundation`)

```kotlin
object ContinuousCurvature {
    fun emit(width: Float, height: Float, radius: Float, receiver: CornerPathReceiver)
    fun emit(
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float,
        receiver: CornerPathReceiver,
    )
}
```

| Parameter      | Type                 | Description                                                                     |
| -------------- | -------------------- | ------------------------------------------------------------------------------- |
| `width`        | `Float`              | Rectangle width in pixels.                                                      |
| `height`       | `Float`              | Rectangle height in pixels.                                                     |
| `radius`       | `Float`              | Uniform corner radius in pixels. Clamped to half the shorter edge.              |
| `topLeft` etc. | `Float`              | Per-corner radii in absolute (visual) order — caller resolves layout direction. |
| `receiver`     | `CornerPathReceiver` | Sink that receives `moveTo`, `lineTo`, `cubicTo`, `close`.                      |

### `CornerPathReceiver` (`:foundation`)

```kotlin
interface CornerPathReceiver {
    fun moveTo(x: Float, y: Float)
    fun lineTo(x: Float, y: Float)
    fun cubicTo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float)
    fun close()
}
```

Implement this interface to forward path commands to your destination path type:
`androidx.compose.ui.graphics.Path`, `android.graphics.Path`, Skia, SVG, or any other path API.
The builders emit one `moveTo` followed by alternating `lineTo` / `cubicTo` calls and exactly
one `close()`.

## Examples

### Compose: ComponentShape (the common case)

```kotlin
// Pill button
.background(Color(colors.primary), CapsuleShape())

// Card with continuous-curvature corners (the default)
.clip(RoundedShape(cornerRadius = 16.dp))

// Asymmetric corners — top corners rounded, bottom flat
.clip(
    UnevenShape(
        topStart = 24.dp,
        topEnd = 24.dp,
    )
)
```

### Compose: per-corner override on an existing shape

```kotlin
val baseShape = RoundedShape(cornerRadius = 12.dp)

// Reuse the existing radii but flatten the bottom-end corner
val tabShape = baseShape.copy(bottomEnd = 0.dp)
```

### Compose: animating between two shapes

```kotlin
val progress by animateFloatAsState(targetValue = if (expanded) 1f else 0f)

val shape = lerp(
    start = CapsuleShape(),
    stop = RoundedShape(cornerRadius = 24.dp),
    fraction = progress,
)
```

### Non-Compose: rendering on `android.graphics.Canvas`

A `CornerPathReceiver` adapter for `android.graphics.Path` lets a system overlay (for example a
dynamic-island view) consume the same squircle geometry without depending on Compose.

```kotlin
import android.graphics.Path
import framework.cortena.ui.shape.ContinuousCurvature
import framework.cortena.ui.shape.CornerPathReceiver

class AndroidPathReceiver(private val path: Path) : CornerPathReceiver {
    override fun moveTo(x: Float, y: Float) {
        path.moveTo(x, y)
    }

    override fun lineTo(x: Float, y: Float) {
        path.lineTo(x, y)
    }

    override fun cubicTo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float) {
        path.cubicTo(x1, y1, x2, y2, x3, y3)
    }

    override fun close() {
        path.close()
    }
}

// In the View
override fun onDraw(canvas: Canvas) {
    val path = Path()
    ContinuousCurvature.emit(
        width = width.toFloat(),
        height = height.toFloat(),
        radius = cornerRadiusPx,
        receiver = AndroidPathReceiver(path),
    )
    canvas.drawPath(path, paint)
}
```

### Non-Compose: per-corner radii

```kotlin
ContinuousCurvature.emit(
    width = bounds.width(),
    height = bounds.height(),
    topLeft = 24f,
    topRight = 24f,
    bottomRight = 32f,
    bottomLeft = 32f,
    receiver = AndroidPathReceiver(path),
)
```
