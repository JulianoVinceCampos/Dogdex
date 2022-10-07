package com.julianovincecampos.dogedex.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
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

@Composable
fun LoginScreen(
    onLoginButtonclick: (String, String) -> Unit,
    onRegisterbuttonClick: () -> Unit,
) {
    Scaffold(
        topBar = { LoginScreentoolbar() }
    ) {
        Content(
            onLoginButtonclick = onLoginButtonclick,
            onRegisterbuttonClick = onRegisterbuttonClick
        )
    }
}

@Composable
private fun Content(
    onLoginButtonclick: (String, String) -> Unit,
    onRegisterbuttonClick: () -> Unit
) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthField(
            label = stringResource(id = R.string.email),
            modifier = Modifier.fillMaxWidth(),
            email = email.value,
            onTextChange = { email.value = it })
        AuthField(
            label = stringResource(id = R.string.password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            email = password.value,
            onTextChange = { password.value = it },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            onClick = { onLoginButtonclick(email.value, password.value) }
        ) {
            Text(
                text = stringResource(id = R.string.login),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), textAlign = TextAlign.Center, text = stringResource(
                id = R.string.do_not_have_an_account
            )
        )

        Text(
            modifier = Modifier
                .clickable(enabled = true, onClick = { onRegisterbuttonClick() })
                .fillMaxWidth()
                .padding(16.dp),
            text = stringResource(id = R.string.register),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun LoginScreentoolbar() {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        backgroundColor = Color.Red,
        contentColor = Color.White,
    )
}
