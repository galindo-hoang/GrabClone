package com.example.user.data.model.mapper

import com.example.user.data.dto.UserDto
import com.example.user.data.model.authentication.User
import com.example.user.utils.EntityMapper

object UserMapper: EntityMapper<User,UserDto> {
    override fun mapFromEntity(entity: User): UserDto {
        return UserDto(
            password = entity.password,
            username = entity.username,
            phoneNumber = entity.phoneNumber
        )
    }

    override fun mapToEntity(entityDto: UserDto): User {
        return User(
            password = entityDto.password,
            username = entityDto.username,
            phoneNumber = entityDto.phoneNumber
        )
    }
}