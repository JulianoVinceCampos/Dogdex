package com.julianovincecampos.dogedex.main

import androidx.camera.core.ImageProxy
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.doglist.DogRepositoryTask
import com.julianovincecampos.dogedex.doglist.DogRespository
import com.julianovincecampos.dogedex.machinelearning.Classifier
import com.julianovincecampos.dogedex.machinelearning.ClassifierRepository
import com.julianovincecampos.dogedex.machinelearning.ClassifierTask
import com.julianovincecampos.dogedex.machinelearning.DogRecognition
import com.julianovincecampos.dogedex.model.Dog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.nio.MappedByteBuffer
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dogRepository: DogRepositoryTask,
    private val classifierRepository: ClassifierTask
) : ViewModel() {

    private val _dog = MutableLiveData<Dog>()
    val dog: LiveData<Dog>
        get() = _dog

    private val _status = MutableLiveData<ApiResponseStatus<Dog>>()
    val status: LiveData<ApiResponseStatus<Dog>>
        get() = _status

    private val _dogRecognition = MutableLiveData<DogRecognition>()
    val dogRecognition: LiveData<DogRecognition>
        get() = _dogRecognition

    fun recognizeImage(imageProxy: ImageProxy) {
        viewModelScope.launch {
            _dogRecognition.value = classifierRepository.recognizeImage(imageProxy)
            imageProxy.close()
        }
    }

    fun getDogByMlId(mlDogId: String) {
        viewModelScope.launch {
            handleResponseStatus(dogRepository.getDogByMlId(mlDogId))
        }
    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<Dog>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _dog.value = apiResponseStatus.data
        }
        _status.value = apiResponseStatus
    }
}
