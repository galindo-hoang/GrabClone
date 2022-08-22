package com.example.driver.domain.usecase

import com.example.driver.data.dto.SubscribeBookingDto
import com.example.driver.data.model.fcm.ResponseUnSubscribe
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.domain.repository.BookingRepository
import com.example.driver.utils.Response
import javax.inject.Inject

class StopListeningBookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(): Response<ResponseUnSubscribe> {
        return try {
            val userDto = authenticationRepository.getAccount()
            val response = bookingRepository.unsubscribeListenBooking(
                SubscribeBookingDto("booking", userDto.username!!)
            )
            when(response.code()){
                200 -> Response.success(response.body()!!)
                401 -> Response.error(null,-2,response.message())
                else -> Response.error(null,response.code(),"fail to connect db")
            }
        } catch (e:Exception){
            Response.error(null,-1,e.message.toString())
        }
    }
}