package com.example.driver.data.repository.booking

import com.example.driver.data.dto.AcceptBooking
import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.SubscribeBookingDto
import com.example.driver.data.dto.UpdateLocation
import com.example.driver.data.model.booking.ResponseAcceptBooking
import com.example.driver.data.model.fcm.ResponseSubscribe
import com.example.driver.data.model.fcm.ResponseUnSubscribe
import com.example.driver.data.model.route.Direction
import retrofit2.Response

interface BookingRemoteDataResource {
    suspend fun subscribeListenBooking(subscribeBookingDto: SubscribeBookingDto): Response<ResponseSubscribe>
    suspend fun unsubscribeListenBooking(subscribeBookingDto: SubscribeBookingDto): Response<ResponseUnSubscribe>
    suspend fun registerFcmToken(registerFCMBody: RegisterFCMBody): Response<Int>
    suspend fun sendCurrentLocation(updateLocation: UpdateLocation): Response<Any>
    suspend fun sendAcceptBooking(acceptBooking: AcceptBooking): Response<ResponseAcceptBooking>
    suspend fun getRoute(method: String, origin: String, destination: String): Response<Direction>
}