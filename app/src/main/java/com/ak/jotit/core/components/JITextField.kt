package com.ak.jotit.core.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ak.jotit.ui.theme.JotItTheme

/**
 * Custom implementation of [OutlinedTextField] to keep style and branding same
 *
 * @param[text] The current text inside input
 * @param[onTextChange] callback invoked when text is changed in input
 * @param[labelTxt] label that shows above input when focused
 * @param[modifier] optional [Modifier] to configure[Composable]
 */
@Composable
fun JITextField(
    text: String,
    onTextChange: (String) -> Unit,
    labelTxt: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {

    Column {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            label = {
                Text(text = labelTxt)
            },
            shape = MaterialTheme.shapes.medium,
            modifier = modifier
//                .heightIn(dimensionResource(id = R.dimen.text_field_height))
                .fillMaxWidth(),
            isError = errorMessage != null,
            visualTransformation = visualTransformation,
            enabled = enabled,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(
                        top = 4.dp,
                        start = 16.dp
                    )
            )
        }
    }
}

@Preview(
    name = "Night Mode - Filled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Day Mode - Filled",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun FilledJITextFieldPreview() {
    JotItTheme {
        Surface {
            JITextField(
                text = "TOA text field",
                onTextChange = {},
                labelTxt = "Label"
            )
        }
    }
}

@Preview(
    name = "Night Mode - Error",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Day Mode - Error",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun ErrorJITextFieldPreview() {
    JotItTheme {
        Surface {
            JITextField(
                text = "TOA text field",
                onTextChange = {},
                labelTxt = "Label",
                errorMessage = "Invalid input"
            )
        }
    }
}

@Preview(
    name = "Night Mode - Empty",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Day Mode - Empty",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun EmptyJITextFieldPreview() {
    JotItTheme {
        Surface {
            JITextField(
                text = "",
                onTextChange = {},
                labelTxt = "Label"
            )
        }
    }
}

