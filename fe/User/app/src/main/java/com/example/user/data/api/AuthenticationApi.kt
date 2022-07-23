package com.example.user.data.api

import com.example.user.data.dto.Login
import com.example.user.data.dto.UserDto
import com.example.user.data.dto.ValidateOTP
import com.example.user.data.model.authentication.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApi {
    @POST("/api/otp/register")
    suspend fun postResponseRegister(
        @Body userDto: UserDto
    ): Response<SuccessBodyValidateOrRegister>

    @POST("/api/otp/validate")
    suspend fun postResponseValidateRegister(
        @Body validateOTP: ValidateOTP
    ): Response<SuccessBodyValidateOrRegister>

    @GET("/refresh-token")
    suspend fun getAccessToken(
        @Query ("Authorization") refreshToken: String
    ): Response<TokenAuthentication>

    @POST("/login")
    suspend fun postResponseLogin(
        @Body login: Login
    ): Response<ResponseLogin>
}