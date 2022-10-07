package com.julianovincecampos.dogedex.viewmodel

import com.julianovincecampos.dogedex.R
import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.auth.AuthTask
import com.julianovincecampos.dogedex.auth.AuthViewModel
import com.julianovincecampos.dogedex.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class AuthViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun testLoginValidationsCorrect() {

        class AuthFakeRepository : AuthTask {

            override suspend fun login(email: String, password: String): ApiResponseStatus<User> {
                return ApiResponseStatus.Success(User(1, "fake@hotmail.com", ""))
            }

            override suspend fun signUp(
                email: String,
                password: String,
                passwordConfirmation: String
            ): ApiResponseStatus<User> {
                return ApiResponseStatus.Success(User(1, "fake@hotmail.com", "15487545481245454"))
            }
        }

        val viewModel = AuthViewModel(authTask = AuthFakeRepository())

        viewModel.login("", "212121")
        assertEquals(R.string.email_is_not_valid, viewModel.emailError.value)

        viewModel.login("fake@hotmail.com", "")
        assertEquals(R.string.password_is_not_valid, viewModel.passwordError.value)

    }

    @Test
    fun testLoginStatesCorrect() {
        val fakeUser = User(1, "fake@hotmail.com", "")

        class AuthFakeRepository : AuthTask {

            override suspend fun login(email: String, password: String): ApiResponseStatus<User> {
                return ApiResponseStatus.Success(fakeUser)
            }

            override suspend fun signUp(
                email: String,
                password: String,
                passwordConfirmation: String
            ): ApiResponseStatus<User> {
                return ApiResponseStatus.Success(User(1, "fake@hotmail.com", "15487545481245454"))
            }
        }

        val viewModel = AuthViewModel(authTask = AuthFakeRepository())

        viewModel.login("fake@hotmail.com", "212121")

        assertEquals(fakeUser.email, viewModel.user.value?.email)
        assertEquals(fakeUser.id, viewModel.user.value?.id)
        assertEquals(fakeUser.authenticationToken, viewModel.user.value?.authenticationToken)

    }

    @Test
    fun testLoginStatesError() {

        class AuthFakeRepository : AuthTask {

            override suspend fun login(email: String, password: String): ApiResponseStatus<User> {
                return ApiResponseStatus.Error(R.string.error_sign_in)
            }

            override suspend fun signUp(
                email: String,
                password: String,
                passwordConfirmation: String
            ): ApiResponseStatus<User> {
                return ApiResponseStatus.Error(12)            }
        }

        val viewModel = AuthViewModel(authTask = AuthFakeRepository())

        viewModel.login("fake@hotmail.com", "212121")

        assert(viewModel.status.value is ApiResponseStatus.Error)

    }

}