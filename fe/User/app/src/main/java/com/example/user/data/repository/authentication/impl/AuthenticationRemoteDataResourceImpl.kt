package com.example.user.data.repository.authentication.impl

import com.example.user.data.api.AuthenticationApi
import com.example.user.data.dto.Login
import com.example.user.data.dto.UserDto
import com.example.user.data.dto.ValidateOTP
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
        userDto: UserDto
    ): Response<SuccessBodyValidateOrRegister> = authenticationApi.postResponseRegister(userDto)

    override suspend fun getResponseValidateRegister(
        validateOTP: ValidateOTP
    ): Response<SuccessBodyValidateOrRegister> =
        authenticationApi.postResponseValidateRegister(validateOTP)

    override suspend fun getResponseLogin(login: Login): Response<ResponseLogin> =
        authenticationApi.postResponseLogin(login)

    override suspend fun getAccessToken(refreshToken: String): Response<TokenAuthentication> =
        authenticationApi.getAccessToken(refreshToken)

}