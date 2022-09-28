package com.julianovincecampos.dogedex.api

import com.julianovincecampos.dogedex.*
import com.julianovincecampos.dogedex.api.ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY
import com.julianovincecampos.dogedex.api.dto.SignInDTO
import com.julianovincecampos.dogedex.api.dto.SignUpDTO
import com.julianovincecampos.dogedex.api.response.DogListApiResponse
import com.julianovincecampos.dogedex.api.response.AuthApiResponse
import com.julianovincecampos.dogedex.api.response.DefaultResponse
import com.julianovincecampos.dogedex.api.response.DogApiResponse
import retrofit2.http.*

interface ApiService {

    @GET(GET_ALL_DOGS_URL)
    suspend fun getAllDogs(): DogListApiResponse

    @POST(SIGN_UP_URL)
    suspend fun singUp(@Body signUpDTO: SignUpDTO): AuthApiResponse

    @POST(SIGN_IN_URL)
    suspend fun singIn(@Body signUpDTO: SignInDTO): AuthApiResponse

    @Headers("${NEEDS_AUTH_HEADER_KEY}:true")
    @POST(ADD_DOG_TO_USER)
    suspend fun addDogToUser(@Body addDogToUserDTO: AddDogTouserDTO): DefaultResponse

    @Headers("${NEEDS_AUTH_HEADER_KEY}: true")
    @GET(GET_USER_DOGS_URL)
    suspend fun getUserDogs(): DogListApiResponse

    @GET(GET_DOG_BY_ML_ID)
    suspend fun getDogByMlId(@Query("ml_id") mlId: String): DogApiResponse

}