package com.example.user.domain.usecase

import android.util.Log
import com.example.user.data.dto.BookingDto
import com.example.user.data.model.booking.ResponseBooking
import com.example.user.domain.repository.AuthenticationRepository
import com.example.user.domain.repository.BookingRepository
import com.example.user.utils.Response
import javax.inject.Inject

class BookingCarUseCase @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(bookingDto: BookingDto): Response<ResponseBooking> {
        return try {
            val userDto = authenticationRepository.getAccount()
            bookingDto.phoneNumber = userDto.phoneNumber.toString()
            bookingDto.username = userDto.username.toString()
            val response = bookingRepository.bookingDriver(bookingDto)
            Log.e("------",response.toString())
             when(response.code()){
                200 -> Response.success(response.body()!!)
                401 -> Response.error(null,-2,response.message())
                else -> Response.error(null, response.code(), message = response.message())
            }
        } catch (e:Exception) { Response.error(null,-1,e.message.toString())}
    }
}