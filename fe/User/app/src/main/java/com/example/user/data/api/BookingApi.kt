package com.example.user.data.api

import com.example.user.data.dto.BookingDto
import com.example.user.data.dto.CancelBookingDto
import com.example.user.data.model.booking.ResponseBooking
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BookingApi {
    @POST("/api/v1/booking/cancel_booking")
    suspend fun cancelBooking(@Body cancelBookingDto: CancelBookingDto): Response<Void>
    @POST("/api/v1/booking/create_booking")
    suspend fun createBooking(@Body bookingDto: BookingDto): Response<ResponseBooking>
}