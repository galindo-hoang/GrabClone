package com.example.user.data.repository.booking.impl

import android.util.Log
import com.example.user.data.api.BookingApi
import com.example.user.data.api.FCMApi
import com.example.user.data.dto.RegisterFCMBody
import com.example.user.data.repository.booking.BookingRemoteDataResource
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

class BookingRemoteDataResourceImpl @Inject constructor(
    private val bookingApi: BookingApi,
    private val fcmApi: FCMApi
): BookingRemoteDataResource {
    override suspend fun bookingDriver(): Response<Int> {
        Log.e("checking----------","a")
        try {
            return bookingApi.checkk()
        }catch (e:Exception) {
            Log.e("+++++",e.message.toString())
            throw e
        }
    }

    override suspend fun registerFcmToken(registerFCMBody: RegisterFCMBody): Response<Int> =
        fcmApi.registerToken(registerFCMBody)
}