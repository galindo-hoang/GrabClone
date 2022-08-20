package com.example.driver.data.repository.authentication

import com.example.driver.data.dto.UserDto
import com.example.driver.data.dto.ValidateOTP
import com.example.driver.data.model.authentication.*
import okhttp3.RequestBody
import retrofit2.Response

interface AuthenticationRemoteDataResource {
    suspend fun getResponseRegisterPhoneNumber(userDto: UserDto): Response<BodyValidateOrRegister>
    suspend fun getResponseRegisterSaveAccount(userDto: UserDto): Response<BodyRegisterSaveAccount>
    suspend fun getResponseValidatePhoneNumber(validateOTP: ValidateOTP): Response<BodyValidateOrRegister>

    suspend fun getResponseLogin(requestBody: RequestBody): Response<ResponseLogin>
    suspend fun getAccessToken(refreshToken: String): Response<TokenAuthentication>
}