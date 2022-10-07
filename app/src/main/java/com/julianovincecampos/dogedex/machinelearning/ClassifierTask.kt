package com.julianovincecampos.dogedex.machinelearning

import androidx.camera.core.ImageProxy


interface ClassifierTask {
    suspend fun recognizeImage(imageProxy: ImageProxy): DogRecognition
}