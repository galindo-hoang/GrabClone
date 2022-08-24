package com.example.driver.data.api

import com.example.driver.data.dto.AcceptBooking
import com.example.driver.data.dto.FinishBooking
import com.example.driver.data.model.booking.ResponseAcceptBooking
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BookingApi {
    @POST("/api/v1/booking/accept_booking")
    suspend fun sendAcceptBooking(@Body acceptBooking: AcceptBooking): Response<ResponseAcceptBooking>
    @POST("/api/v1/booking/finish_ride")
    suspend fun finishBooking(@Body finishBooking: FinishBooking): Response<String>
}