package com.example.driver.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface BookingApi {
    @POST("/acceptBooking")
    suspend fun sendAcceptBooking(): Response<Boolean>
}