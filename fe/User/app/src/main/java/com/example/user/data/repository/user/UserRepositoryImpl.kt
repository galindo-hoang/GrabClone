package com.example.user.data.repository.user

import com.example.user.data.api.UserApi
import com.example.user.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
): UserRepository {
}