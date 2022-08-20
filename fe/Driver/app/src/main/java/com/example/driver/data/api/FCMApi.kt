package com.example.driver.data.api

import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.UpdateLocation
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FCMApi {
    @POST("/api/v1/fcm/register")
    suspend fun registerToken(@Body registerFCMBody: RegisterFCMBody): Response<Int>
    @POST("/api/v1/fcm/update")
    fun sendCurrentLocation(@Body updateLocation: UpdateLocation): Response<Any>
}