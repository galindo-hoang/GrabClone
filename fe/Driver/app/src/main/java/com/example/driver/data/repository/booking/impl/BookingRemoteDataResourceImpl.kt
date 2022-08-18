package com.example.driver.data.repository.booking.impl

import com.example.driver.data.api.BookingApi
import com.example.driver.data.api.FCMApi
import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.UpdateLocation
import com.example.driver.data.repository.booking.BookingRemoteDataResource
import retrofit2.Response
import javax.inject.Inject

class BookingRemoteDataResourceImpl @Inject constructor(
    private val bookingApi: BookingApi,
    private val fcmApi: FCMApi
): BookingRemoteDataResource {
    override suspend fun bookingDriver(): Response<Int> {
        try {
            return bookingApi.checkk()
        }catch (e:Exception) {
            throw e
        }
    }

    override suspend fun registerFcmToken(registerFCMBody: RegisterFCMBody): Response<Int> =
        fcmApi.registerToken(registerFCMBody)

    override suspend fun sendCurrentLocation(updateLocation: UpdateLocation): Response<Any> =
        fcmApi.sendCurrentLocation(updateLocation)

    override suspend fun sendAcceptBooking(): Response<Boolean> = bookingApi.sendAcceptBooking()

}