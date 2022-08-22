package com.example.driver.data.api

import com.example.driver.data.dto.AcceptBooking
import com.example.driver.data.model.booking.ResponseAcceptBooking
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BookingApi {
    @POST("/acceptBooking")
    suspend fun sendAcceptBooking(
        @Body acceptBooking: AcceptBooking
    ): Response<ResponseAcceptBooking>
}