package com.example.driver.data.api

import com.example.driver.data.dto.*
import com.example.driver.data.model.authentication.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {
    @POST("/register")
    suspend fun postResponseRegisterSaveAccount(
        @Body userDto: UserDto
    ): Response<BodyRegisterSaveAccount>

    @POST("/api/v1/sms/register")
    suspend fun postResponseRegisterPhoneNumber(
        @Body userDto: UserDto
    ): Response<BodyValidateOrRegister>

    @POST("/api/v1/sms/validate")
    suspend fun postResponseValidatePhoneNumber(
        @Body validateOTP: ValidateOTP
    ): Response<BodyValidateOrRegister>

    @POST("/login")
    suspend fun postResponseLogin(
        @Body requestBody: RequestBody
    ): Response<ResponseLogin>
}