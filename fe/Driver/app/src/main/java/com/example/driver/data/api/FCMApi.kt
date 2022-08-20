package com.example.driver.data.api

import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.SubscribeBookingDto
import com.example.driver.data.dto.UpdateLocation
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FCMApi {
    @POST("/api/v1/fcm-publish/register")
    suspend fun registerToken(@Body registerFCMBody: RegisterFCMBody): Response<Int>
    @POST("/api/v1/fcm/update")
    fun sendCurrentLocation(@Body updateLocation: UpdateLocation): Response<Any>
    @POST("/api/v1/fcm-publish/subscribe")
    fun subscribeListeningBooking(@Body subscribeBookingDto: SubscribeBookingDto): Response<Any>
    @POST("/api/v1/fcm-publish/unsubscribe")
    fun unsubscribeListeningBooking(@Body subscribeBookingDto: SubscribeBookingDto): Response<Any>
}