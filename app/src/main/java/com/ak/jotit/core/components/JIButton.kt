package com.ak.jotit.core.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ak.jotit.ui.theme.JotItTheme

@Composable
fun JIButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ButtonWithIconContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = if (small) {
            modifier.heightIn(min = ButtonDefaults.MinHeight)
        } else {
            modifier
        },
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding,
        content = {
            content()
        }
    )
}

@Preview(
    name = "Normal Button light",
    group = "normal",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Normal Button dark",
    group = "normal",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun JINormalButtonPreview() {
    JotItTheme {
        Surface {
            JIButton(
                onClick = {},
                small = false
            ) {
                Text(text = "Button text")
            }
        }
    }
}

@Preview(
    name = "Small Button light",
    group = "small",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Small Button dark",
    group = "small",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun JISmallButtonPreview() {
    JotItTheme {
        Surface {
            JIButton(
                onClick = {},
                small = true
            ) {
                Text(text = "Button text")
            }
        }
    }
}
