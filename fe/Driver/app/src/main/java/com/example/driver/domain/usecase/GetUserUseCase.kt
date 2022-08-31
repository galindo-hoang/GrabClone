package com.example.driver.domain.usecase

import com.example.driver.data.dto.UserDto
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.utils.Response
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(): Response<UserDto> {
        return try {
            val response = authenticationRepository.getAccount()
            Response.success(response)
        } catch (e:Exception) { Response.error(null,-1,e.message.toString()) }
    }
}