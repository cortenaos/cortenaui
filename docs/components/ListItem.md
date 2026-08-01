# ListItem

## Concept

`ListItem` is a standard list row component typically used inside a `ListView`. It provides
structured slots for **leading** content (icons, avatars), a **title**, an optional **subtitle**,
and **trailing** content (chevrons, toggles, values).

When placed inside a `ListView`, `ListItem` automatically reports whether it has a `leading` slot
so that separators can adjust their inset accordingly.

## API Reference

```kotlin
@Composable
fun ListItem(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
)
```

### Parameters

| Name       | Type                                 | Default    | Description                                                    |
| ---------- | ------------------------------------ | ---------- | -------------------------------------------------------------- |
| `title`    | `@Composable () -> Unit`             | -          | Primary text or content of the item.                           |
| `modifier` | `Modifier`                           | `Modifier` | Standard Compose modifier.                                     |
| `subtitle` | `(@Composable () -> Unit)?`          | `null`     | Optional secondary text below the title.                       |
| `leading`  | `(@Composable () -> Unit)?`          | `null`     | Content placed at the start of the item (e.g., Icon, avatar).  |
| `trailing` | `(@Composable RowScope.() -> Unit)?` | `null`     | Content placed at the end of the item (e.g., Toggle, chevron). |
| `onClick`  | `(() -> Unit)?`                      | `null`     | If provided, the item becomes clickable with this callback.    |

### Layout

The internal layout is a horizontal `Row` with a minimum height of **52.dp** and **16.dp**
horizontal / **12.dp** vertical padding:

```plain
┌──────────────────────────────────────────────────┐
│  [leading]  16dp  [title     ]  16dp  [trailing] │
│                   [subtitle  ]                   │
└──────────────────────────────────────────────────┘
```

## Examples

### Title only

```kotlin
ListItem(title = { Text("Notifications", role = TextRole.BodyMedium) })
```

### With leading icon

```kotlin
ListItem(
    title = { Text("Wi-Fi", role = TextRole.BodyMedium) },
    leading = {
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Regular.WifiHigh),
            contentDescription = null,
        )
    },
)
```

### With subtitle

```kotlin
ListItem(
    title = { Text("Battery", role = TextRole.BodyMedium) },
    subtitle = { Text("85%", role = TextRole.BodySmall) },
    leading = {
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Regular.Battery),
            contentDescription = null,
        )
    },
)
```

### With trailing content and click handler

```kotlin
ListItem(
    title = { Text("Dark Mode", role = TextRole.BodyMedium) },
    trailing = {
        Toggle(checked = isDark, onCheckedChange = { isDark = it })
    },
    onClick = { isDark = !isDark },
)
```

### Navigation item with chevron

```kotlin
ListItem(
    title = { Text("About", role = TextRole.BodyMedium) },
    trailing = {
        Icon(
            renderer = PhosphorIcon(PhosphorIcons.Bold.CaretRight),
            contentDescription = null,
            size = 14.dp,
        )
    },
    onClick = { onNavigate("about") },
)
```
