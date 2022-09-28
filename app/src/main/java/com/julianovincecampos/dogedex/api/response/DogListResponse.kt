package com.julianovincecampos.dogedex.api.response

import com.julianovincecampos.dogedex.api.dto.DogDTO
import com.squareup.moshi.Json


class DogListResponse(@field:Json(name = "dogs") val dogsDTO: List<DogDTO>)