package com.example.user.domain.usecase

import com.example.user.domain.repository.BookingRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
//    fun invoke(accessToken: String): User = User("")
}