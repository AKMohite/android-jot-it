package com.ak.jotit.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ak.jotit.R
import com.ak.jotit.core.components.JIButton
import com.ak.jotit.core.components.JITextField

/**
 * Reference:
 * - [How to Validate Forms with Clean Architecture (You're Doing it Wrong)](https://www.youtube.com/watch?v=zu8lQSVw4vk)
 * - [Advanced Form Operations in Jetpack Compose](https://www.section.io/engineering-education/jetpack-compose-forms/)
 * - [INPUT VALIDATION IN JETPACK COMPOSE](https://www.droidcon.com/2021/11/08/input-validation-in-jetpack-compose/)
 * - [Effective state management for TextField in Compose](https://medium.com/androiddevelopers/effective-state-management-for-textfield-in-compose-d6e5b070fbe5)
 * */
@Composable
internal fun LoginScreen() {
    val keyboardController = LocalSoftwareKeyboardController.current
    val viewModel: LoginViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    LoginContent(
        state = state,
        onEmailChange = viewModel::onEmailInput,
        onPasswordChange = viewModel::onPasswordInput,
        onLogin = {
            keyboardController?.hide()
            viewModel.onLogin()
        }
    )
}

@Composable
private fun LoginContent(
    state: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit
) {

    val localFocusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        JITextField(
            modifier = Modifier.fillMaxWidth(),
            text = state.email,
            onTextChange = onEmailChange,
            labelTxt = stringResource(id = R.string.email),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        JITextField(
            modifier = Modifier.fillMaxWidth(),
            text = state.password,
            onTextChange = onPasswordChange,
            labelTxt = stringResource(id = R.string.password),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    localFocusManager.clearFocus()
                    onLogin()
                }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        JIButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onLogin
        ) {
            Text(text = stringResource(id = R.string.login_btn))
        }
    }

}
