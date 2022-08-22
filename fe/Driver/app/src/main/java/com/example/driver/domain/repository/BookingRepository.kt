package com.example.driver.domain.repository

import android.webkit.WebStorage
import androidx.navigation.NavDestination
import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.SubscribeBookingDto
import com.example.driver.data.dto.UpdateLocation
import com.example.driver.data.model.route.Direction
import retrofit2.Response

interface BookingRepository {
    suspend fun subscribeListenBooking(subscribeBookingDto: SubscribeBookingDto): Response<Any>
    suspend fun unsubscribeListenBooking(subscribeBookingDto: SubscribeBookingDto): Response<Any>
    suspend fun postRegisterFcmToken(registerFCMBody: RegisterFCMBody): Response<Int>
    suspend fun updateCurrentLocation(updateLocation: UpdateLocation): Response<Any>
    suspend fun sendAcceptBooking(): Response<Boolean>
    suspend fun getRouteNavigation(method: String, origin: String, destination: String): Response<Direction>
}