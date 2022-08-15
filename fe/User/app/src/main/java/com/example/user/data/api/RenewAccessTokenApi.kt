package com.example.user.data.api

import com.example.user.data.model.authentication.TokenAuthentication
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface RenewAccessTokenApi {
    @GET("/refresh-token")
    suspend fun getNewAccessToken(
        @Header("Authorization") refreshToken: String
    ): Response<TokenAuthentication>
}