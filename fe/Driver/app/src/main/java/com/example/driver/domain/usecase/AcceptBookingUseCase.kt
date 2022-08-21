package com.example.driver.domain.usecase

import com.example.driver.domain.repository.BookingRepository
import com.example.driver.utils.Response
import javax.inject.Inject

class AcceptBookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend fun invoke(): Response<Int>{
        val response = bookingRepository.sendAcceptBooking()
        return when(response.code()){
            200 -> {
                val body = response.body()
                if(body != null) Response.success(1)
                else Response.success(-1)
            }
            401 -> {
                Response.error(null,-2,response.message())
            }
            else -> Response.error(null,response.code(),"fail to connect db")
        }
    }
}