package com.julianovincecampos.dogedex

import androidx.camera.core.ImageProxy
import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.doglist.DogRepositoryTask
import com.julianovincecampos.dogedex.machinelearning.ClassifierTask
import com.julianovincecampos.dogedex.machinelearning.DogRecognition
import com.julianovincecampos.dogedex.main.MainViewModel
import com.julianovincecampos.dogedex.model.Dog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun test() {

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

        class ClassifierTaskFake : ClassifierTask {
            override suspend fun recognizeImage(imageProxy: ImageProxy): DogRecognition {
                TODO("Not yet implemented")
            }
        }

        val viewModel = MainViewModel(
            dogRepository = FakeDogRepository(),
            classifierRepository = ClassifierTaskFake()
        )
        viewModel.getDogByMlId("")

        Assert.assertEquals(viewModel.getDogByMlId("") , null)
        assert(viewModel.status.value is ApiResponseStatus.Success)
    }

}

