package com.julianovincecampos.dogedex.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.auth.AuthNavDestinations.LoginScreenDestionation
import com.julianovincecampos.dogedex.auth.AuthNavDestinations.SignUpScreenDestionation
import com.julianovincecampos.dogedex.composables.ErrorDialog
import com.julianovincecampos.dogedex.composables.LoadingWheel
import com.julianovincecampos.dogedex.model.User

@Composable
fun AuthScreen(
    status: ApiResponseStatus<User>?,
    onLoginButtonClick: (String, String) -> Unit,
    onSignUpButtonClick: (email: String, password: String, passwordConfirmation: String) -> Unit,
    onErrorDialogDismiss: () -> Unit,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    AuthNavHost(
        navController = navController,
        onLoginButtonClick = onLoginButtonClick,
        onSignUpButtonClick = onSignUpButtonClick,
        authViewModel
    )

    if (status is ApiResponseStatus.Loading) {
        LoadingWheel()
    } else if (status is ApiResponseStatus.Error) {
        ErrorDialog(messageId = status.messageId, onErrorDialogDismiss)
    }
}

@Composable
private fun AuthNavHost(
    navController: NavHostController,
    onLoginButtonClick: (String, String) -> Unit,
    onSignUpButtonClick: (email: String, password: String, passwordConfirmation: String) -> Unit,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = LoginScreenDestionation
    ) {
        composable(route = LoginScreenDestionation) {
            LoginScreen(
                onLoginButtonclick = onLoginButtonClick,
                onRegisterbuttonClick = { navController.navigate(route = SignUpScreenDestionation) }
            )
        }

        composable(route = SignUpScreenDestionation) {
            SingUpScreen(
                onSignUpButtonClick = onSignUpButtonClick,
                onNavigationIconClick = {
                    navController.navigateUp()
                },
                authViewModel = authViewModel
            )
        }
    }
}