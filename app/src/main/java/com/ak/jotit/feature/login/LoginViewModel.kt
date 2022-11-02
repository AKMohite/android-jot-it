package com.ak.jotit.feature.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val SAVED_LOGIN_EMAIL = "saved_login_email"
private const val SAVED_LOGIN_PASSWORD = "saved_login_password"
@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val handle: SavedStateHandle
): ViewModel(), ILoginActions {

    private val emailText = handle.getStateFlow(SAVED_LOGIN_EMAIL, "")
    private val passwordText = handle.getStateFlow(SAVED_LOGIN_PASSWORD, "") // TODO you should not store secret info in SavedStateHandle
    private val modelState: MutableStateFlow<LoginState.ModelState> = MutableStateFlow(LoginState.ModelState())

//    region login state
    val uiState: StateFlow<LoginState> = combine(
        emailText,
        passwordText,
        modelState
    ) { email: String, password: String, modelState: LoginState.ModelState ->
        LoginState(
            email = email,
            emailHasError = modelState.emailHasError,
            password = password,
            passwordHasError = modelState.passwordHasError
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LoginState())
//    endregion

    override fun onEmailInput(text: String) {
        handle[SAVED_LOGIN_EMAIL] = text
        modelState.value = modelState.value.copy(emailHasError = false)
    }

    override fun onPasswordInput(text: String) {
        handle[SAVED_LOGIN_PASSWORD] = text
        modelState.value = modelState.value.copy(passwordHasError = false)
    }

    override fun onLogin() {}

}

internal interface ILoginActions {
    fun onEmailInput(text: String)
    fun onPasswordInput(text: String)
    fun onLogin()
}

internal class LoginState(
    val email: String = "",
    private val emailHasError: Boolean = false,
    val password: String = "",
    private val passwordHasError: Boolean = false,
    val isSubmitEnabled: Boolean = !emailHasError && !passwordHasError
) {
    internal data class ModelState(
        val emailHasError: Boolean = false,
        val passwordHasError: Boolean = false,
        val isSubmitEnabled: Boolean = false
    )
}