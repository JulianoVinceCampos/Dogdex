package com.julianovincecampos.dogedex.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julianovincecampos.dogedex.R
import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authTask: AuthTask
) : ViewModel() {

    var user = mutableStateOf<User?>(null)
        private set

    var status = mutableStateOf<ApiResponseStatus<User>?>(null)
        private set

    var emailError = mutableStateOf<Int?>(null)
        private set

    var passwordError = mutableStateOf<Int?>(null)
        private set

    var confirmPasswordError = mutableStateOf<Int?>(null)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            status.value = ApiResponseStatus.Loading()
            when {
                email.isEmpty() -> {
                    emailError.value = R.string.email_is_not_valid
                }
                password.isEmpty() -> {
                    passwordError.value = R.string.password_is_not_valid
                }
                else -> {
                    val user = authTask.login(email, password)
                    hanlderResponseStatus(user)
                }
            }

        }
    }

    fun signUp(email: String, password: String, passwordConfirmation: String) {
        when {
            email.isEmpty() -> {
                emailError.value = R.string.email_is_not_valid
            }
            password.isEmpty() -> {
                passwordError.value = R.string.password_is_not_valid
            }
            passwordConfirmation.isEmpty() -> {
                passwordError.value = R.string.passwords_do_not_match
                confirmPasswordError.value = R.string.passwords_do_not_match
            }
            else -> {
                viewModelScope.launch {
                    status.value = ApiResponseStatus.Loading()
                    val user = authTask.signUp(email, password, passwordConfirmation)
                    hanlderResponseStatus(user)
                }
            }
        }
    }

    private fun hanlderResponseStatus(apiResponseStatus: ApiResponseStatus<User>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            user.value = apiResponseStatus.data
        } else {
            status.value = apiResponseStatus
        }
    }

    fun resetApiResponseStatus() {
        status.value = null
    }

    fun restErrors() {
        emailError.value = null
        passwordError.value = null
        confirmPasswordError.value = null
    }

}