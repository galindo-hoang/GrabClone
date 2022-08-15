package com.example.user.domain.usecase

import com.example.user.data.dto.BookingDto
import com.example.user.domain.repository.BookingRepository
import retrofit2.Response
import javax.inject.Inject

class BookingCarUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend fun invoke(bookingDto: BookingDto): Response<Int> {
        return bookingRepository.bookingDriver(bookingDto)
    }
}