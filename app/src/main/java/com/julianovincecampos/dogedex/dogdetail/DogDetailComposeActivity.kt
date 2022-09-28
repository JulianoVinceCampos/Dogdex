package com.julianovincecampos.dogedex.dogdetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import coil.annotation.ExperimentalCoilApi
import com.julianovincecampos.dogedex.R
import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.dogdetail.ui.theme.DogedexTheme
import com.julianovincecampos.dogedex.model.Dog
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoilApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class DogDetailComposeActivity : ComponentActivity() {

    companion object {
        const val DOG_KEY = "dog"
        const val IS_RECOGNITION_KEY = "is_recognition"
    }

    private val viewModel: DogDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)
        val isRecognition = intent?.extras?.getBoolean(IS_RECOGNITION_KEY, false) ?: false

        if (dog == null) {
            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setContent {

            val status = viewModel.status

            if (status.value is ApiResponseStatus.Success) {
                finish()
            } else {
                DogedexTheme {
                    DogDetailScreen(
                        dog = dog,
                        status = status.value,
                        onButtonClicked = {
                            onButtonclicked(isRecognition = isRecognition, dogId = dog.id)
                        },
                        onErrorDialogDismiss = ::resetApiResponseStatus
                    )
                }
            }
        }
    }

    private fun resetApiResponseStatus() {
        viewModel.resetApiResponseStatus()
    }

    private fun onButtonclicked(isRecognition: Boolean, dogId: Long) {
        if (isRecognition) {
            viewModel.addDogToUser(dogId)
        } else {
            finish()
        }
    }
}