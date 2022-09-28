package com.julianovincecampos.dogedex.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import com.julianovincecampos.dogedex.LABEL_PATH
import com.julianovincecampos.dogedex.MODEL_PATH
import com.julianovincecampos.dogedex.machinelearning.Classifier
import com.julianovincecampos.dogedex.api.ApiServiceInterceptor
import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.auth.LoginActivity
import com.julianovincecampos.dogedex.databinding.ActivityMainBinding
import com.julianovincecampos.dogedex.dogdetail.DogDetailComposeActivity
import com.julianovincecampos.dogedex.doglist.DogListActivity
import com.julianovincecampos.dogedex.machinelearning.DogRecognition
import com.julianovincecampos.dogedex.model.Dog
import com.julianovincecampos.dogedex.model.User
import com.julianovincecampos.dogedex.settings.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.support.common.FileUtil
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private var isCameraReady = false
    private val viewmodel: MainViewModel by viewModels()

    private val requestPermissionLaucher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            setupCamera()
        } else {
            Toast.makeText(
                this,
                "You need to accept camera permission to user camera",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = User.getLoggedInUser(this)
        if (user == null) {
            openLoginActivity()
            return
        } else {
            ApiServiceInterceptor.setSessionToken(user.authenticationToken)
        }

        binding.settingsFab.setOnClickListener {
            openSettingsActivity()
        }

        binding.dogListFab.setOnClickListener {
            openDogListActivity()
        }

        viewmodel.status.observe(this) { status ->
            when (status) {
                is ApiResponseStatus.Error -> {
                    Toast.makeText(this, status.messageId, Toast.LENGTH_SHORT).show()
                }
//                is ApiResponseStatus.Loading -> binding.loadingWhell.visibility = View.VISIBLE
//                is ApiResponseStatus.Success -> binding.loadingWhell.visibility = View.GONE
            }
        }

        viewmodel.dog.observe(this) { dog ->
            if (dog != null) {
                openDogDetailActivity(dog)
            }
        }

        viewmodel.dogRecognition.observe(this) {
            enableTakePhotoButton(dogRecognition = it)
        }

        requestCameraPermission()
    }

    private fun openDogDetailActivity(dog: Dog) {
        val intent = Intent(this, DogDetailComposeActivity::class.java)
        intent.putExtra(DogDetailComposeActivity.DOG_KEY, dog)
        intent.putExtra(DogDetailComposeActivity.IS_RECOGNITION_KEY, true)
        startActivity(intent)
    }
    
    private fun openDogListActivity() {
        startActivity(Intent(this, DogListActivity::class.java))
    }

    fun openSettingsActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun openLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun setupCamera() {
        binding.cameraPreview.post {
            imageCapture =
                ImageCapture.Builder().setTargetRotation(binding.cameraPreview.display.rotation)
                    .build()
            cameraExecutor = Executors.newSingleThreadExecutor()
            startCamera()
            isCameraReady = true
        }
    }

    private fun requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    setupCamera()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                    AlertDialog.Builder(this).setTitle("Aceite por favor")
                        .setMessage("Aceite a permissão de camera")
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            requestPermissionLaucher.launch(android.Manifest.permission.CAMERA)
                        }
                        .setNegativeButton(android.R.string.cancel) { _, _ ->
                        }.show()
                }

                else -> {
                    requestPermissionLaucher.launch(android.Manifest.permission.CAMERA)
                }
            }
        } else {
            setupCamera()
        }
    }

    private fun startCamera() {
        val cameraProviderFutre = ProcessCameraProvider.getInstance(this)

        cameraProviderFutre.addListener({
            val cameraProvider = cameraProviderFutre.get()

            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                viewmodel.recognizeImage(imageProxy)
//                imageProxy.close()
            }

            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalysis
            )
        }, ContextCompat.getMainExecutor(this))
    }

    private fun enableTakePhotoButton(dogRecognition: DogRecognition) {
        if (dogRecognition.confidence > 70.0) {
            binding.takePhotoFav.alpha = 1f
            binding.takePhotoFav.setOnClickListener {
                viewmodel.getDogByMlId(dogRecognition.id)
            }
        } else {
            binding.takePhotoFav.alpha = 0.2f
            binding.takePhotoFav.setOnClickListener(null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraExecutor.isInitialized)
            cameraExecutor.shutdown()
    }
}