package com.julianovincecampos.dogedex.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import coil.annotation.ExperimentalCoilApi
import com.julianovincecampos.dogedex.dogdetail.ui.theme.DogedexTheme
import com.julianovincecampos.dogedex.main.MainActivity
import com.julianovincecampos.dogedex.model.User
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val user = authViewModel.user
            val status = authViewModel.status

            val userValue = user.value

            if (userValue != null) {
                User.setLoggedInUser(this, userValue)
                startMainActivity()
            }
            DogedexTheme {
                AuthScreen(
                    status = status.value,
                    onSignUpButtonClick = { email, password, passwordConfirmation ->
                        authViewModel.signUp(
                            email = email,
                            password = password,
                            passwordConfirmation = passwordConfirmation
                        )
                    },
                    onErrorDialogDismiss = ::resetApiResponseStatus,
                    onLoginButtonClick = { email, password ->
                        authViewModel.login(
                            email,
                            password
                        )
                    },
                    authViewModel = authViewModel
                )
            }
        }
    }

    private fun resetApiResponseStatus() {
        authViewModel.resetApiResponseStatus()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}