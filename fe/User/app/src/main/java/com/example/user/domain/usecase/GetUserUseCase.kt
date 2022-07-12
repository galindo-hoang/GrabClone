package com.example.user.domain.usecase

import com.example.user.data.model.User
import com.example.user.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun invoke(accessToken: String): User = User("")
}