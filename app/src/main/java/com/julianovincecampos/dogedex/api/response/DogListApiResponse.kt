package com.julianovincecampos.dogedex.api.response

import com.squareup.moshi.Json

class DogListApiResponse(
    val message: String,
    @field:Json(name = "is_success") val isSuccess: Boolean,
    @field:Json(name = "data")val data: DogListResponse
)
