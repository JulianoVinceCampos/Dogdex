package com.julianovincecampos.dogedex

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.doglist.DogListScreem
import com.julianovincecampos.dogedex.doglist.DogListViewModel
import com.julianovincecampos.dogedex.doglist.DogRepositoryTask
import com.julianovincecampos.dogedex.model.Dog
import org.junit.Rule
import org.junit.Test

class DogListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
    @Test
    fun progressBarShowsWhenLoadingState() {

        class FakeRepository : DogRepositoryTask {

            override suspend fun getDogsCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Loading()
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = DogListViewModel(
            FakeRepository()
        )

        composeTestRule.setContent {
            DogListScreem(
                onNavigationIconClick = { },
                onDogClicked = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag(testTag = "loading-wheel").assertIsDisplayed()

    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
    @Test
    fun errorDialogShowsIfErrorGettingDogs() {

        class FakeRepository : DogRepositoryTask {

            override suspend fun getDogsCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Error(messageId = R.string.there_was_an_error)
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = DogListViewModel(
            FakeRepository()
        )

        composeTestRule.setContent {
            DogListScreem(
                onNavigationIconClick = { },
                onDogClicked = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag(testTag = "error-dialog").assertIsDisplayed()

    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
    @Test
    fun dogListShowsIfSuccessGettingDogs() {
        val dog1Name = "fakenamedog"
        val dog2Name = "fakenamedog_2"

        class FakeRepository : DogRepositoryTask {

            override suspend fun getDogsCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Success(
                    listOf<Dog>(
                        Dog(
                            0, 0,
                            dog1Name,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            inCollection = true
                        ),
                        Dog(
                            1, 1,
                            dog2Name,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            inCollection = true
                        )
                    )
                )
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = DogListViewModel(
            FakeRepository()
        )

        composeTestRule.setContent {
            DogListScreem(
                onNavigationIconClick = { },
                onDogClicked = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("TESTE")

        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "dog-${dog1Name}")
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "dog-${dog2Name}")
            .assertIsDisplayed()

    }

}