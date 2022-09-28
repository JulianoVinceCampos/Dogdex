package com.julianovincecampos.dogedex

import com.julianovincecampos.dogedex.api.ApiService
import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.api.dto.DogDTO
import com.julianovincecampos.dogedex.api.dto.SignInDTO
import com.julianovincecampos.dogedex.api.dto.SignUpDTO
import com.julianovincecampos.dogedex.api.response.*
import com.julianovincecampos.dogedex.doglist.DogRespository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Test
import org.junit.Assert.*

class DogRepositoryTest {

    @Test
    fun testGetDogCollectionSucess() = runBlocking {
        class FakeApiService : ApiService {
            override suspend fun getAllDogs(): DogListApiResponse {
                return DogListApiResponse(
                    message = "", isSuccess = true, DogListResponse(
                        listOf(
                            DogDTO(
                                0, 0,
                                "defaultnomedogs",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                            ),
                            DogDTO(
                                20, 20,
                                "fakenamedogs",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                            )
                        )
                    )
                )
            }

            override suspend fun singUp(signUpDTO: SignUpDTO): AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun singIn(signUpDTO: SignInDTO): AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun addDogToUser(addDogToUserDTO: AddDogTouserDTO): DefaultResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getUserDogs(): DogListApiResponse {
                return DogListApiResponse(
                    message = "", isSuccess = true, DogListResponse(
                        listOf(
                            DogDTO(
                                20, 20,
                                "fakenamedogs",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                            )
                        )
                    )
                )
            }

            override suspend fun getDogByMlId(mlId: String): DogApiResponse {
                TODO("Not yet implemented")
            }

        }

        val dogRepository = DogRespository(
            apiService = FakeApiService(),
            dispatcher = TestCoroutineDispatcher()
        )

        val apiResponseStatus = dogRepository.getDogsCollection()
        assert(apiResponseStatus is ApiResponseStatus.Success)
        assert(apiResponseStatus is ApiResponseStatus.Success)

        val dogCollection = (apiResponseStatus as ApiResponseStatus.Success).data
        assertEquals(2, dogCollection.size)
        assertEquals("fakenamedogs", dogCollection[1].nameDog)

    }
}