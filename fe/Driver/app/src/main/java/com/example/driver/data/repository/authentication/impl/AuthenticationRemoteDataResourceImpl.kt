package com.example.driver.data.repository.authentication.impl

import com.example.driver.data.api.AuthenticationApi
import com.example.driver.data.api.RenewAccessTokenApi
import com.example.driver.data.dto.UserDto
import com.example.driver.data.dto.ValidateOTP
import com.example.driver.data.model.authentication.*
import com.example.driver.data.repository.authentication.AuthenticationRemoteDataResource
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRemoteDataResourceImpl @Inject constructor(
    private val authenticationApi: AuthenticationApi,
    private val renewAccessTokenApi: RenewAccessTokenApi
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
        renewAccessTokenApi.getNewAccessToken("Bearer $refreshToken")

}