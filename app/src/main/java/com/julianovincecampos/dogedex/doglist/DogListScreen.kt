package com.julianovincecampos.dogedex.doglist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.julianovincecampos.dogedex.R
import com.julianovincecampos.dogedex.api.dto.ApiResponseStatus
import com.julianovincecampos.dogedex.composables.BackNavigationIcon
import com.julianovincecampos.dogedex.composables.ErrorDialog
import com.julianovincecampos.dogedex.composables.LoadingWheel
import com.julianovincecampos.dogedex.model.Dog

private const val GRID_SPAN_COUNT = 3

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun DogListScreem(
    onNavigationIconClick: () -> Unit,
    onDogClicked: (Dog) -> Unit, viewModel: DogListViewModel = hiltViewModel()
) {
    val status = viewModel.status.value
    val dogList = viewModel.dogList.value

    Scaffold(
        topBar = { DogListScreenTopBar(onNavigationIconClick) }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(GRID_SPAN_COUNT),
            content = {
                items(dogList) {
                    DogGridItem(dog = it, onDogClicked = onDogClicked)
                }
            })
    }

    if (status is ApiResponseStatus.Loading) {
        LoadingWheel()
    } else if (status is ApiResponseStatus.Error) {
        ErrorDialog(messageId = status.messageId) { viewModel.resetApiResponseStatus() }
    }
}

@Composable
fun DogListScreenTopBar(onClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.my_dog_collection)) },
        backgroundColor = Color.White,
        contentColor = Color.Black,
        navigationIcon = { BackNavigationIcon(onClick) }
    )
}

@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun DogGridItem(dog: Dog, onDogClicked: (Dog) -> Unit) {
    if (dog.inCollection) {
        Surface(
            modifier = Modifier
                .padding(8.dp)
                .height(100.dp)
                .width(100.dp),
            onClick = { onDogClicked(dog) },
            shape = RoundedCornerShape(4.dp)
        ) {
            Image(
                painter = rememberImagePainter(dog.imageUrl),
                contentDescription = null,
                modifier = Modifier.background(Color.White)
            )
        }
    } else {
        Surface(
            modifier = Modifier
                .padding(8.dp)
                .height(100.dp)
                .width(100.dp),
            color = Color.Red,
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = dog.index.toString(),
                color = Color.White,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                fontSize = 42.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}