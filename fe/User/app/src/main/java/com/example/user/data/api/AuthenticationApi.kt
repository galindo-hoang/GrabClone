package com.example.user.data.api

import com.example.user.data.model.authentication.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApi {
    @POST("/api/otp/register")
    suspend fun postResponseRegister(
        @Body postValidateRegister: PostValidateRegister
    ): Response<ResponseRegister>

    @POST("/api/otp/validate")
    suspend fun postResponseValidateRegister(
        @Body postValidateRegister: PostValidateRegister
    ): Response<ResponseValidateRegister>

    @GET("/refresh-token")
    suspend fun getAccessToken(
        @Query ("Authorization") refreshToken: String
    ): Response<TokenAuthentication>

    @POST("/login")
    suspend fun postResponseLogin(
        @Body postLogin: PostLogin
    ): Response<ResponseLogin>
}