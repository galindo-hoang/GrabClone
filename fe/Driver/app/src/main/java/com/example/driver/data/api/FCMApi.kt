package com.example.driver.data.api

import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.SubscribeBookingDto
import com.example.driver.data.dto.UpdateLocation
import com.example.driver.data.model.booking.ResponseRegisterFcmToken
import com.example.driver.data.model.booking.ResponseUpdateLocation
import com.example.driver.data.model.fcm.ResponseSubscribe
import com.example.driver.data.model.fcm.ResponseUnSubscribe
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FCMApi {
    @POST("/api/v1/fcm-publish/register")
    suspend fun registerToken(@Body registerFCMBody: RegisterFCMBody): Response<ResponseRegisterFcmToken>
    @POST("/api/v1/booking/update_driver_location")
    suspend fun sendCurrentLocationAfterAccept(@Body updateLocation: UpdateLocation): Response<ResponseUpdateLocation>
    @POST("/api/v1/booking/update_location")
    suspend fun sendCurrentLocationBeforeAccept(@Body updateLocation: UpdateLocation): Response<ResponseUpdateLocation>
    @POST("/api/v1/fcm-publish/subscribe")
    suspend fun subscribeListeningBooking(@Body subscribeBookingDto: SubscribeBookingDto): Response<ResponseSubscribe>
    @POST("/api/v1/fcm-publish/unsubscribe")
    suspend fun unsubscribeListeningBooking(@Body subscribeBookingDto: SubscribeBookingDto): Response<ResponseUnSubscribe>
}