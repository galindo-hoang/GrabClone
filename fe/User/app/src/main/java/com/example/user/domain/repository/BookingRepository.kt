package com.example.user.domain.repository

import com.example.user.data.dto.BookingDto
import retrofit2.Response

interface BookingRepository {
    suspend fun bookingDriver(bookingDto: BookingDto): Response<Int>
}