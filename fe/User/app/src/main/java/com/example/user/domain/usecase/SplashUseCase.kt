package com.example.user.domain.usecase

import com.example.user.domain.repository.AuthenticationRepository
import com.example.user.utils.Response
import javax.inject.Inject

class SplashUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(): Int{
        val token = authenticationRepository.getNumberToken()
        val account = authenticationRepository.getNumberAccount()
        return try {
            if (token == 1 && account == 1) {
                authenticationRepository.getAccount()
                1
            }
            else -1
        } catch (e: Exception) { -1 }
    }
}