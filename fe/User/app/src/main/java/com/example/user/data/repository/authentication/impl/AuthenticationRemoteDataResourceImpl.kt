package com.example.user.data.repository.authentication.impl

import com.example.user.data.api.AuthenticationApi
import com.example.user.data.model.authentication.*
import com.example.user.data.repository.authentication.AuthenticationRemoteDataResource
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRemoteDataResourceImpl @Inject constructor(
    private val authenticationApi: AuthenticationApi
): AuthenticationRemoteDataResource {
    override suspend fun getResponseRegister(
        postValidateRegister: PostValidateRegister
    ): Response<ResponseRegister> = authenticationApi.postResponseRegister(postValidateRegister)

    override suspend fun getResponseValidateRegister(
        postValidateRegister: PostValidateRegister
    ): Response<ResponseValidateRegister> =
        authenticationApi.postResponseValidateRegister(postValidateRegister)

    override suspend fun getResponseLogin(postLogin: PostLogin): Response<ResponseLogin> =
        authenticationApi.postResponseLogin(postLogin)

    override suspend fun getAccessToken(refreshToken: String): Response<TokenAuthentication> =
        authenticationApi.getAccessToken(refreshToken)

}