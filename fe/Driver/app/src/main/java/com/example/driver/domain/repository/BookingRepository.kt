package com.example.driver.domain.repository

import com.example.driver.data.dto.*
import com.example.driver.data.model.booking.ResponseAcceptBooking
import com.example.driver.data.model.booking.ResponseRegisterFcmToken
import com.example.driver.data.model.booking.ResponseUpdateLocation
import com.example.driver.data.model.fcm.ResponseSubscribe
import com.example.driver.data.model.fcm.ResponseUnSubscribe
import com.example.driver.data.model.route.Direction
import retrofit2.Response

interface BookingRepository {
    suspend fun subscribeListenBooking(subscribeBookingDto: SubscribeBookingDto): Response<ResponseSubscribe>
    suspend fun unsubscribeListenBooking(subscribeBookingDto: SubscribeBookingDto): Response<ResponseUnSubscribe>
    suspend fun postRegisterFcmToken(registerFCMBody: RegisterFCMBody): Response<ResponseRegisterFcmToken>
    suspend fun updateCurrentLocation(updateLocation: UpdateLocation): Response<ResponseUpdateLocation>
    suspend fun sendAcceptBooking(acceptBooking: AcceptBooking): Response<ResponseAcceptBooking>
    suspend fun getRouteNavigation(method: String, origin: String, destination: String): Response<Direction>
    suspend fun sendFinishRoute(finishBooking: FinishBooking): Response<String>
}