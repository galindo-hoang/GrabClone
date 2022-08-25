package com.example.user.domain.usecase

import android.util.Log
import com.example.user.data.dto.BookingDto
import com.example.user.data.dto.CancelBookingDto
import com.example.user.data.model.booking.ResponseBooking
import com.example.user.domain.repository.AuthenticationRepository
import com.example.user.domain.repository.BookingRepository
import com.example.user.utils.Response
import javax.inject.Inject

class CancelBookingCarUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val bookingRepository: BookingRepository
) {
    suspend fun invoke(): Response<ResponseBooking> {
        return try {
            val userDto = authenticationRepository.getAccount()
            val response = bookingRepository.cancelBookingDriver(CancelBookingDto(userDto.username!!))
            Log.e("8",response.toString())
             when(response.code()){
                200 -> {
                    Log.e("8",response.body().toString())
                    Response.success(response.body()!!)
                }
                401 -> Response.error(null,-2,response.message())
                else -> Response.error(null, response.code(), message = response.message())
            }
        } catch (e:Exception) {
            Log.e("8",e.message.toString())
            Response.error(null,-1,e.message.toString())
        }
    }
}