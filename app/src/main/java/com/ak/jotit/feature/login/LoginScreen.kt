package com.ak.jotit.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
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
internal fun LoginScreen(
    onAuthenticate: () -> Unit,
    onSignup: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val viewModel: LoginViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            onAuthenticate()
        }
    }

    LoginContent(
        state = state,
        onEmailChange = viewModel::onEmailInput,
        onPasswordChange = viewModel::onPasswordInput,
        onLogin = {
            keyboardController?.hide()
            viewModel.onLogin()
        },
        onSignup = {
            keyboardController?.hide()
            onSignup()
        }
    )
}

@Composable
private fun LoginContent(
    state: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onSignup: () -> Unit
) {

    val localFocusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            contentDescription = "App Logo",
            painter = painterResource(id = R.drawable.ic_note),
            modifier = Modifier
                .padding(top = 60.dp)
                .requiredSize(92.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.FillBounds
        )

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
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    localFocusManager.clearFocus()
                    onLogin()
                }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        JIButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                localFocusManager.clearFocus()
                onLogin()
            }
        ) {
            Text(text = stringResource(id = R.string.login_btn))
        }

        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.login_signup_btn))
                addStyle(SpanStyle(color = MaterialTheme.colorScheme.primary), 23, this.length)
                toAnnotatedString()
            },
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End)
                .clickable(onClick = onSignup),
        )
    }

}
