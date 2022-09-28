package com.julianovincecampos.dogedex.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AuthField(
    label: String,
    email: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorMessageId: Int? = null
) {
    Column(modifier = modifier) {
        if (errorMessageId != null) {
            Text(text = stringResource(id = errorMessageId))
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text((label)) },
            value = email,
            onValueChange = onTextChange,
            visualTransformation = visualTransformation,
            isError = errorMessageId != null
        )
    }
}