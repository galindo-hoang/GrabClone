package com.example.user.data.repository.user

import com.example.user.data.api.AuthenticationApi
import com.example.user.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: AuthenticationApi
): UserRepository {
}