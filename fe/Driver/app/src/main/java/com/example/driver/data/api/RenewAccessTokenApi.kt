package com.example.driver.data.api

import com.example.driver.data.model.authentication.TokenAuthentication
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface RenewAccessTokenApi {
    @GET("/refresh-token")
    suspend fun getNewAccessToken(
        @Header("Authorization") refreshToken: String
    ): Response<TokenAuthentication>
}