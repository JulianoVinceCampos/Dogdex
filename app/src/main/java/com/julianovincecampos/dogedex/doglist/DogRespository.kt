package com.julianovincecampos.dogedex.doglist

import com.julianovincecampos.dogedex.AddDogTouserDTO
import com.julianovincecampos.dogedex.R
import com.julianovincecampos.dogedex.api.ApiService
import com.julianovincecampos.dogedex.model.Dog
import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.api.dto.DogDTOMapper
import com.julianovincecampos.dogedex.api.makeNetWorkCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class DogRespository @Inject constructor(
    private val apiService: ApiService,
    private val dispatcher: CoroutineDispatcher
) : DogRepositoryTask {

    override suspend fun getDogsCollection(): ApiResponseStatus<List<Dog>> {

        return withContext(dispatcher) {
            val allDogsListDeferred = async { downloadDogs() }
            val userDogListDeferred = async { getUserDogs() }

            val allDogsListResponse = allDogsListDeferred.await()
            val userDogsListResponse = userDogListDeferred.await()

            when {
                allDogsListResponse is ApiResponseStatus.Error -> {
                    allDogsListResponse
                }
                userDogsListResponse is ApiResponseStatus.Error -> {
                    userDogsListResponse
                }
                allDogsListResponse is ApiResponseStatus.Success && userDogsListResponse is ApiResponseStatus.Success -> {
                    ApiResponseStatus.Success(
                        getCollectionList(
                            allDogsListResponse.data,
                            userDogsListResponse.data
                        )
                    )
                }
                else -> {
                    ApiResponseStatus.Error(R.string.unknown_error)
                }
            }
        }
    }

    private fun getCollectionList(allDogList: List<Dog>, userDogList: List<Dog>): List<Dog> {
        return allDogList.map {
            if (userDogList.contains(it)) {
                it
            } else {
                Dog(
                    0, it.index,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    inCollection = false
                )
            }
        }.sorted()
    }

    suspend fun downloadDogs(): ApiResponseStatus<List<Dog>> {
        return makeNetWorkCall {
            val dogListResponse = apiService.getAllDogs()
            val dogDTOList = dogListResponse.data.dogsDTO
            val dogDTOMapper = DogDTOMapper()
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
        }
    }

    override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> = makeNetWorkCall {
        val addDogToUserDTO = AddDogTouserDTO(dogId)
        val defaultResponse = apiService.addDogToUser(addDogToUserDTO)

        if (!defaultResponse.isSuccess) {
            throw Exception(defaultResponse.message)
        }
    }

    private suspend fun getUserDogs(): ApiResponseStatus<List<Dog>> = makeNetWorkCall {
        val dogListApiResponse = apiService.getUserDogs()
        val dogDTOList = dogListApiResponse.data.dogsDTO
        if (!dogListApiResponse.isSuccess) {
            throw Exception(dogListApiResponse.message)
        }
        val dogDTOManager = DogDTOMapper()
        dogDTOManager.fromDogDTOListToDogDomainList(dogDTOList)
    }

    override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> = makeNetWorkCall {
        val response = apiService.getDogByMlId(mlDogId)

        if (!response.isSuccess) {
            throw Exception(response.message)
        }

        val dogDTOMapper = DogDTOMapper()
        dogDTOMapper.fromDogDTOToDogDomain(response.data.dog)
    }

}
