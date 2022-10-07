package com.julianovincecampos.dogedex.doglist

import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.model.Dog

interface DogRepositoryTask {

    suspend fun getDogsCollection(): ApiResponseStatus<List<Dog>>

    suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any>

    suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog>

}
