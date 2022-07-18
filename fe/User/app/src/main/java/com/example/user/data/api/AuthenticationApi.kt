package com.example.user.data.api

import com.example.user.data.model.authentication.PostValidateRegister
import com.example.user.data.model.authentication.ResponseRegister
import com.example.user.data.model.authentication.ResponseValidateRegister
import com.example.user.data.model.authentication.TokenAuthentication
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApi {
    @POST("/api/otp/register")
    suspend fun getResponseRegister(
        @Body postValidateRegister: PostValidateRegister
    ): Response<ResponseRegister>

    @POST("/api/otp/validate")
    suspend fun getResponseValidateRegister(
        @Body postValidateRegister: PostValidateRegister
    ): Response<ResponseValidateRegister>

    @GET("/refresh-token")
    suspend fun get(
        @Query ("Authorization") a: String
    ): Response<TokenAuthentication>

//    @GET("/login")
//    suspend fun check(
//        @Query ("Authorization") a: String
//    ): Response<TokenAuthentication>
}