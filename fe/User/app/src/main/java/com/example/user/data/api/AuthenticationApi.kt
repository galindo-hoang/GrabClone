package com.example.user.data.api

import com.example.user.data.dto.UserDto
import com.example.user.data.dto.ValidateOTP
import com.example.user.data.model.authentication.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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