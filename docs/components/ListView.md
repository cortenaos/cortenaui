# ListView

## Concept

`ListView` is a card-like container that groups a set of `ListItem` rows into a rounded, visually
distinct section.

Items are registered through a scope builder — call `item { }` inside the `content` lambda for each row.

## API Reference

```kotlin
@Composable
fun ListView(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: ListViewScope.() -> Unit,
)
```

### ListViewScope

```kotlin
interface ListViewScope {
    fun item(content: @Composable () -> Unit)
}
```

### Parameters

| Name       | Type            | Default    | Description                                          |
| ---------- | --------------- | ---------- | ---------------------------------------------------- |
| `modifier` | `Modifier`      | `Modifier` | Standard Compose modifier.                           |
| `title`    | `String?`       | `null`     | Optional section header (rendered uppercase).        |
| `content`  | `ListViewScope` | —          | Scope builder. Call `item { }` to register each row. |

## Examples

### Basic grouped list

```kotlin
ListView(title = "Colors") {
    item {
        ListItem(
            title = { Text("Red", role = TextRole.BodyMedium) },
            leading = { Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color.Red)) },
        )
    }
    item {
        ListItem(
            title = { Text("Blue", role = TextRole.BodyMedium) },
            leading = { Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color.Blue)) },
        )
    }
}
```

### Programmatic item generation

```kotlin
ListView(title = "FANCY") {
    colors.forEach { (name, color) ->
        item {
            ListItem(
                title = { Text(name, role = TextRole.BodyMedium) },
                leading = {
                    Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(color))
                },
            )
        }
    }
}
```
