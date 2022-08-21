package com.example.user.data.api

import com.example.user.data.dto.RegisterFCMBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FCMApi {
    @POST("/api/v1/fcm-publish/register")
    suspend fun registerToken(@Body registerFCMBody: RegisterFCMBody): Response<Int>
}