package com.julianovincecampos.dogedex.auth

import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.model.User

interface AuthTask {

    suspend fun login(email: String, password: String): ApiResponseStatus<User>

    suspend fun signUp(
        email: String,
        password: String,
        passwordConfirmation: String
    ): ApiResponseStatus<User>

}
