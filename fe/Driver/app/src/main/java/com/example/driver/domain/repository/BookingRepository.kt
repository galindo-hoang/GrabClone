package com.example.driver.domain.repository

import android.webkit.WebStorage
import androidx.navigation.NavDestination
import com.example.driver.data.dto.AcceptBooking
import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.SubscribeBookingDto
import com.example.driver.data.dto.UpdateLocation
import com.example.driver.data.model.booking.ResponseAcceptBooking
import com.example.driver.data.model.fcm.ResponseSubscribe
import com.example.driver.data.model.fcm.ResponseUnSubscribe
import com.example.driver.data.model.route.Direction
import retrofit2.Response

interface BookingRepository {
    suspend fun subscribeListenBooking(subscribeBookingDto: SubscribeBookingDto): Response<ResponseSubscribe>
    suspend fun unsubscribeListenBooking(subscribeBookingDto: SubscribeBookingDto): Response<ResponseUnSubscribe>
    suspend fun postRegisterFcmToken(registerFCMBody: RegisterFCMBody): Response<Int>
    suspend fun updateCurrentLocation(updateLocation: UpdateLocation): Response<Any>
    suspend fun sendAcceptBooking(acceptBooking: AcceptBooking): Response<ResponseAcceptBooking>
    suspend fun getRouteNavigation(method: String, origin: String, destination: String): Response<Direction>
}