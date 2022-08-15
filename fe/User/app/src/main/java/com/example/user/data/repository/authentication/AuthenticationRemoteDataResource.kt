package com.example.user.data.repository.authentication

import com.example.user.data.dto.UserDto
import com.example.user.data.dto.ValidateOTP
import com.example.user.data.model.authentication.*
import okhttp3.RequestBody
import retrofit2.Response

interface AuthenticationRemoteDataResource {
    suspend fun getResponseRegisterPhoneNumber(userDto: UserDto): Response<BodyValidateOrRegister>
    suspend fun getResponseRegisterSaveAccount(userDto: UserDto): Response<BodyRegisterSaveAccount>
    suspend fun getResponseValidatePhoneNumber(validateOTP: ValidateOTP): Response<BodyValidateOrRegister>

    suspend fun getResponseLogin(requestBody: RequestBody): Response<ResponseLogin>
    suspend fun getAccessToken(refreshToken: String): Response<TokenAuthentication>
}