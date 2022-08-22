package com.example.driver.data.repository.booking

import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.SubscribeBookingDto
import com.example.driver.data.dto.UpdateLocation
import com.example.driver.data.model.route.Direction
import retrofit2.Response

interface BookingRemoteDataResource {
    suspend fun subscribeListenBooking(subscribeBookingDto: SubscribeBookingDto): Response<Any>
    suspend fun unsubscribeListenBooking(subscribeBookingDto: SubscribeBookingDto): Response<Any>
    suspend fun registerFcmToken(registerFCMBody: RegisterFCMBody): Response<Int>
    suspend fun sendCurrentLocation(updateLocation: UpdateLocation): Response<Any>
    suspend fun sendAcceptBooking(): Response<Boolean>
    suspend fun getRoute(method: String, origin: String, destination: String): Response<Direction>
}