package com.example.driver.data.model.mapper

import com.example.driver.data.dto.UserDto
import com.example.driver.data.model.authentication.User
import com.example.driver.utils.EntityMapper

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