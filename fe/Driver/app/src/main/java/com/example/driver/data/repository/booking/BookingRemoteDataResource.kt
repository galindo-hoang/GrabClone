package com.example.driver.data.repository.booking

import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.UpdateLocation
import retrofit2.Response

interface BookingRemoteDataResource {
    suspend fun bookingDriver(): Response<Int>
    suspend fun registerFcmToken(registerFCMBody: RegisterFCMBody): Response<Int>
    suspend fun sendCurrentLocation(updateLocation: UpdateLocation): Response<Any>
    suspend fun sendAcceptBooking(): Response<Boolean>
}