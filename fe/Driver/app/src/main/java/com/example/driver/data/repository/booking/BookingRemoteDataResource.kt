package com.example.driver.data.repository.booking

import retrofit2.Response

interface BookingRemoteDataResource {
    suspend fun bookingDriver(): Response<Int>
}