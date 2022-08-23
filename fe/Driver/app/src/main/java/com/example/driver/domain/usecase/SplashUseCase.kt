package com.example.driver.domain.usecase

import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.utils.Response
import javax.inject.Inject

class SplashUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(): Response<Int> {
        return try {
            val tokenSize = authenticationRepository.getNumberToken()
            val accountSize = authenticationRepository.getNumberAccount()
            if (tokenSize == 1 && accountSize == 1) {
                authenticationRepository.getAccount()
                Response.success(1)
            }
            else Response.success(-1)
        } catch (e: Exception) { Response.success(-1) }
    }
}