package com.example.user.domain.usecase

import com.example.user.domain.repository.AuthenticationRepository
import com.example.user.utils.Response
import javax.inject.Inject

class SplashUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(): Response<Int> {
        return try {
            val token = authenticationRepository.getNumberToken()
            val account = authenticationRepository.getNumberAccount()
            if (token == 1 && account == 1) {
                authenticationRepository.getAccount()
                Response.success(1)
            }
            else Response.success(-1)
        } catch (e: Exception) { Response.error(null,-1,e.message.toString()) }
    }
}