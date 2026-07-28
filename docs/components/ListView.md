# ListView

iOS-style inset grouped list with automatic separators between items.

## Concept

`ListView` is a card-like container that groups a set of `ListItem` rows into a rounded, visually
distinct section — similar to the grouped list style found in iOS Settings pages.

Key features:

- **Automatic separators**: A `Separator` is drawn between each pair of items. Controlled via `showSeparators` and `separatorPadding`.
- **Section header / footer**: Optional `title` (rendered uppercase above the card) and `footer` (below the card).
- **Rounded card styling**: Content is clipped to `RoundedShape(24.dp)` with `surfaceVariant` background.

Items are registered through a scope builder — call `item { }` inside the `content` lambda for each row.

## API Reference

```kotlin
@Composable
fun ListView(
    modifier: Modifier = Modifier,
    title: String? = null,
    footer: String? = null,
    showSeparators: Boolean = true,
    separatorPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
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

| Name               | Type            | Default                             | Description                                          |
| ------------------ | --------------- | ----------------------------------- | ---------------------------------------------------- |
| `modifier`         | `Modifier`      | `Modifier`                          | Standard Compose modifier.                           |
| `title`            | `String?`       | `null`                              | Optional section header (rendered uppercase).        |
| `footer`           | `String?`       | `null`                              | Optional section footer text.                        |
| `showSeparators`   | `Boolean`       | `true`                              | Draws a `Separator` between each pair of items.      |
| `separatorPadding` | `PaddingValues` | `PaddingValues(horizontal = 16.dp)` | Padding applied to each auto-inserted separator.     |
| `content`          | `ListViewScope` | —                                   | Scope builder. Call `item { }` to register each row. |

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

### Without separators

```kotlin
ListView(title = "Compact", showSeparators = false) {
    items.forEach { label ->
        item { ListItem(title = { Text(label, role = TextRole.BodyMedium) }) }
    }
}
```

### With footer

```kotlin
ListView(
    title = "Account",
    footer = "Your account data is stored locally on this device.",
) {
    item { ListItem(title = { Text("Name", role = TextRole.BodyMedium) }, trailing = { Text("John") }) }
    item { ListItem(title = { Text("Email", role = TextRole.BodyMedium) }, trailing = { Text("john@example.com") }) }
}
```
