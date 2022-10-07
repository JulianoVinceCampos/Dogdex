package com.julianovincecampos.dogedex.auth

import com.julianovincecampos.dogedex.api.ApiService
import com.julianovincecampos.dogedex.model.User
import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.api.dto.SignInDTO
import com.julianovincecampos.dogedex.api.dto.SignUpDTO
import com.julianovincecampos.dogedex.api.dto.UserDTOMapper
import com.julianovincecampos.dogedex.api.makeNetWorkCall
import java.lang.Exception
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) : AuthTask {

    override suspend fun login(email: String, password: String): ApiResponseStatus<User> =
        makeNetWorkCall {
            val signInDTO = SignInDTO(email, password)
            val signInResponse = apiService.singIn(signInDTO)

            if (!signInResponse.isSuccess) {
                throw Exception(signInResponse.message)
            }

            val userDTO = signInResponse.data.user
            val userDTOMapper = UserDTOMapper()
            userDTOMapper.fromUserDTOToUserDomain(userDTO)
        }

    override suspend fun signUp(
        email: String,
        password: String,
        passwordConfirmation: String
    ): ApiResponseStatus<User> = makeNetWorkCall {
        val signUpDTO = SignUpDTO(email, password, passwordConfirmation)
        val signUpResponse = apiService.singUp(signUpDTO)

        if (!signUpResponse.isSuccess) {
            throw Exception(signUpResponse.message)
        }

        val userDTO = signUpResponse.data.user
        val userDTOMapper = UserDTOMapper()
        userDTOMapper.fromUserDTOToUserDomain(userDTO)
    }

}
