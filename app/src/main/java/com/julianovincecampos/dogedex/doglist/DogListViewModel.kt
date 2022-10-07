package com.julianovincecampos.dogedex.doglist

import androidx.compose.runtime.mutableStateOf
import com.julianovincecampos.dogedex.model.Dog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogListViewModel @Inject constructor(
    val dogRespository: DogRepositoryTask
) : ViewModel() {

    var dogList = mutableStateOf<List<Dog>>(listOf())
        private set

    var status = mutableStateOf<ApiResponseStatus<Any>?>(null)
        private set

    init {
        getDogCollections()
    }

    private fun getDogCollections() {
        viewModelScope.launch {
            status.value = ApiResponseStatus.Loading()
            handleResponseStatus(dogRespository.getDogsCollection())
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<List<Dog>>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            dogList.value = apiResponseStatus.data
        }
        status.value = apiResponseStatus as ApiResponseStatus<Any>
    }

    fun resetApiResponseStatus() {
        status.value = null
    }

}
