package com.example.user.data.repository.booking

import com.example.user.data.dto.RegisterFCMBody
import retrofit2.Response

interface BookingRemoteDataResource {
    suspend fun bookingDriver(): Response<Int>
    suspend fun registerFcmToken(registerFCMBody: RegisterFCMBody): Response<Int>
}