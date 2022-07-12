package com.example.user.domain.usecase

import com.example.user.domain.repository.UserRepository
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun invoke(refreshToken: String): String = ""
}