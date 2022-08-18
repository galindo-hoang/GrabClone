package com.example.driver.domain.repository

import com.example.driver.data.dto.BookingDto
import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.UpdateLocation
import retrofit2.Response

interface BookingRepository {
    suspend fun bookingDriver(bookingDto: BookingDto): Response<Int>
    suspend fun postRegisterFcmToken(registerFCMBody: RegisterFCMBody): Response<Int>
    suspend fun updateCurrentLocation(updateLocation: UpdateLocation): Response<Any>
    suspend fun sendAcceptBooking(): Response<Boolean>
}