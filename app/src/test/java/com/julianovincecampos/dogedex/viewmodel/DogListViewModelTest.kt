package com.julianovincecampos.dogedex.viewmodel

import com.julianovincecampos.dogedex.R
import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.doglist.DogListViewModel
import com.julianovincecampos.dogedex.doglist.DogRepositoryTask
import com.julianovincecampos.dogedex.model.Dog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class DogListViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun downloadDogListStatusSucess() {

        class FakeDogRepository : DogRepositoryTask {

            override suspend fun getDogsCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Success(
                    listOf<Dog>(
                        Dog(
                            0, 0,
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
                        ),
                        Dog(
                            1, 1,
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
                    )
                )
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                return ApiResponseStatus.Success(Unit)
            }

            override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> {
                return ApiResponseStatus.Success(
                    Dog(
                        11, 11,
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
                )
            }

        }

        val viewModel = DogListViewModel(dogRespository = FakeDogRepository())

        assertEquals(2, viewModel.dogList.value.size)
        assert(viewModel.status.value is ApiResponseStatus.Success)
    }

    @Test
    fun downloadDogListStatusError() {

        class FakeDogRepository : DogRepositoryTask {

            override suspend fun getDogsCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Error(R.string.unknown_error)
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                return ApiResponseStatus.Success(Unit)
            }

            override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> {
                return ApiResponseStatus.Error(
                    R.string.unknown_error
                )
            }

        }

        val viewModel = DogListViewModel(dogRespository = FakeDogRepository())

        assertEquals(0, viewModel.dogList.value.size)
        assert(viewModel.status.value is ApiResponseStatus.Error)
    }

    @Test
    fun resetStatusCorrect() {

        class FakeDogRepository : DogRepositoryTask {

            override suspend fun getDogsCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Error(R.string.unknown_error)
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                return ApiResponseStatus.Success(Unit)
            }

            override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> {
                return ApiResponseStatus.Error(
                    R.string.unknown_error
                )
            }

        }

        val viewModel = DogListViewModel(dogRespository = FakeDogRepository())

        assert(viewModel.status.value is ApiResponseStatus.Error)
        viewModel.resetApiResponseStatus()
        assert(viewModel.status.value == null)
    }

}