package com.example.user.data.repository.booking

import com.example.user.data.dto.BookingDto
import com.example.user.data.dto.CancelBookingDto
import com.example.user.data.dto.RegisterFCMBody
import com.example.user.data.model.booking.ResponseBooking
import com.example.user.data.model.fcm.ResponseRegisterFcmToken
import retrofit2.Response

interface BookingRemoteDataResource {
    suspend fun bookingDriver(bookingDto: BookingDto): Response<ResponseBooking>
    suspend fun cancelBookingDriver(cancelBookingDto: CancelBookingDto): Response<Void>
    suspend fun registerFcmToken(registerFCMBody: RegisterFCMBody): Response<ResponseRegisterFcmToken>
}