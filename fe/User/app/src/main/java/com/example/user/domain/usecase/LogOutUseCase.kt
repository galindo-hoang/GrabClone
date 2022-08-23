package com.example.user.domain.usecase

import com.example.user.domain.repository.AuthenticationRepository
import com.example.user.utils.Response
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(): Response<Int> {
        return try {
            authenticationRepository.clearAll()
            Response.success(1)
        }catch (e:Exception){
            Response.error(null,-1,e.message.toString())
        }
    }
}