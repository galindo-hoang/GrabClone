package com.example.user.data.repository.authentication

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationCacheDataResource: AuthenticationCacheDataResource,
    private val authenticationLocalDataResource: AuthenticationLocalDataResource,
    private val authenticationRemoteDataResource: AuthenticationRemoteDataResource
) {
}