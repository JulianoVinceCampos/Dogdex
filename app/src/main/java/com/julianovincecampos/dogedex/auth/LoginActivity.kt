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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DogedexTheme {
                AuthScreen(
                    onUserLoggedIn = ::startMainActivity
                )
            }
        }
    }

    private fun startMainActivity(userValue: User) {
        User.setLoggedInUser(this, userValue)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}