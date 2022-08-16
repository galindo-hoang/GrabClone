package com.example.driver.data.repository.booking.impl

import android.util.Log
import com.example.driver.data.api.BookingApi
import com.example.driver.data.repository.booking.BookingRemoteDataResource
import retrofit2.Response
import javax.inject.Inject

class BookingRemoteDataResourceImpl @Inject constructor(
    private val bookingApi: BookingApi
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
}