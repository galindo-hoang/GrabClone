package com.example.driver.domain.repository

import com.example.driver.data.dto.BookingDto
import retrofit2.Response

interface BookingRepository {
    suspend fun bookingDriver(bookingDto: BookingDto): Response<Int>
}