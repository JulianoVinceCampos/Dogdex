package com.julianovincecampos.dogedex.api.dto

import com.julianovincecampos.dogedex.model.User

class UserDTOMapper {
    fun fromUserDTOToUserDomain(userDTO: UserDTO): User = User(
        userDTO.id,
        userDTO.email,
        userDTO.authenticationToken
    )
}
