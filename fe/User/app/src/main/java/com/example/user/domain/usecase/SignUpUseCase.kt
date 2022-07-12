package com.example.user.domain.usecase

import com.example.user.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun invoke(): String = ""
}