package com.example.driver.domain.usecase

import com.example.driver.data.dto.AcceptBooking
import com.example.driver.data.model.booking.ResponseAcceptBooking
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.domain.repository.BookingRepository
import com.example.driver.utils.Response
import javax.inject.Inject

class AcceptBookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(id: Int): Response<ResponseAcceptBooking>{
        val response = bookingRepository.sendAcceptBooking(
            AcceptBooking(id, authenticationRepository.getAccount().username.toString())
        )
        return try {
            when(response.code()){
                200 -> { Response.success(response.body()!!) }
                401 -> { Response.error(null,-2,response.message()) }
                else -> Response.error(null,response.code(),response.message())
            }
        } catch (e:Exception) { Response.error(null,-1,e.message.toString())}
    }
}