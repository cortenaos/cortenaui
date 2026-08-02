package framework.cortena.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import framework.cortena.ui.theme.LocalSpacing

@Composable
fun SafeArea(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = LocalSpacing.current.Md.dp, // default: 16dp
    verticalPadding: Dp = LocalSpacing.current.None.dp, // default: 0dp
    content: @Composable () -> Unit,
) {
    val appBarPadding = framework.cortena.ui.layout.LocalAppBarPadding.current

    Box(
        modifier =
            modifier
                .safeDrawingPadding()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                .padding(top = appBarPadding)
    ) {
        content()
    }
}
