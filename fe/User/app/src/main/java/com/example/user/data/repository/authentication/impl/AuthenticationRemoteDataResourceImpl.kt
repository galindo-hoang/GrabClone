package com.example.user.data.repository.authentication.impl

import com.example.user.data.api.AuthenticationApi
import com.example.user.data.dto.UserDto
import com.example.user.data.dto.ValidateOTP
import com.example.user.data.model.authentication.*
import com.example.user.data.repository.authentication.AuthenticationRemoteDataResource
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRemoteDataResourceImpl @Inject constructor(
    private val authenticationApi: AuthenticationApi
): AuthenticationRemoteDataResource {
    override suspend fun getResponseRegisterPhoneNumber(
        userDto: UserDto
    ): Response<BodyValidateOrRegister> = authenticationApi.postResponseRegisterPhoneNumber(userDto)

    override suspend fun getResponseRegisterSaveAccount(
        userDto: UserDto
    ): Response<BodyRegisterSaveAccount> = authenticationApi.postResponseRegisterSaveAccount(userDto)

    override suspend fun getResponseValidatePhoneNumber(
        validateOTP: ValidateOTP
    ): Response<BodyValidateOrRegister> =
        authenticationApi.postResponseValidatePhoneNumber(validateOTP)

    override suspend fun getResponseLogin(requestBody: RequestBody): Response<ResponseLogin> =
        authenticationApi.postResponseLogin(requestBody)

    override suspend fun getAccessToken(refreshToken: String): Response<TokenAuthentication> =
        authenticationApi.getAccessToken(refreshToken)

}