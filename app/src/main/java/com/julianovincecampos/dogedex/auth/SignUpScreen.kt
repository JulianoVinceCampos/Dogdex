package com.julianovincecampos.dogedex.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.julianovincecampos.dogedex.R
import com.julianovincecampos.dogedex.composables.AuthField
import com.julianovincecampos.dogedex.composables.BackNavigationIcon

@Composable
fun SingUpScreen(
    onSignUpButtonClick: (email: String, password: String, passwordConfirmation: String) -> Unit,
    onNavigationIconClick: () -> Unit,
    authViewModel: AuthViewModel
) {
    Scaffold(topBar = { SignUpScreenToolbar(onNavigationIconClick) }) {
        Content(
            restFieldErrros = { authViewModel.restErrors() },
            onSignUpButtonClick = onSignUpButtonClick,
            authViewModel = authViewModel
        )
    }
}

@Composable
fun SignUpScreenToolbar(
    onNavigationIconClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        backgroundColor = Color.Red,
        contentColor = Color.White,
        navigationIcon = { BackNavigationIcon { onNavigationIconClick() } }
    )
}

@Composable
private fun Content(
    restFieldErrros: () -> Unit,
    onSignUpButtonClick: (email: String, password: String, passwordConfirmation: String) -> Unit,
    authViewModel: AuthViewModel
) {

    val email = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    val confirmPassword = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        AuthField(
            label = stringResource(id = R.string.email),
            email = email.value,
            onTextChange = {
                email.value = it
                restFieldErrros()
            },
            modifier = Modifier.fillMaxWidth(),
            errorMessageId = authViewModel.emailError.value
        )

        AuthField(
            label = stringResource(id = R.string.password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            email = password.value,
            onTextChange = {
                password.value = it
                restFieldErrros()
            },
            visualTransformation = PasswordVisualTransformation(),
            errorMessageId = authViewModel.passwordError.value
        )

        AuthField(
            label = stringResource(id = R.string.confirm_password),
            email = confirmPassword.value,
            onTextChange = {
                confirmPassword.value = it
                restFieldErrros()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            visualTransformation = PasswordVisualTransformation(),
            errorMessageId = authViewModel.confirmPasswordError.value
        )

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            onClick = { onSignUpButtonClick(email.value, password.value, confirmPassword.value) }) {
            Text(
                text = stringResource(id = R.string.sign_up),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
