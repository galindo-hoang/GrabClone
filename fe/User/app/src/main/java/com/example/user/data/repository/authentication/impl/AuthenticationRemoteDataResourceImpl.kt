package com.example.user.data.repository.authentication.impl

import com.example.user.data.api.AuthenticationApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRemoteDataResourceImpl @Inject constructor(
    private val authenticationApi: AuthenticationApi
) {
}