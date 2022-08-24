package com.example.driver.domain.usecase

import android.util.Log
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
    suspend fun invoke(): Response<String> {
        return try {
            val userDto = authenticationRepository.getAccount()
            val response = bookingRepository.unsubscribeListenBooking(SubscribeBookingDto("booking", userDto.username!!))
            Log.e("9",response.toString())
            when(response.code()){
                200 -> {
                    Log.e("9",response.body().toString())
                    Response.success("Done stop listening")
                }
                401 -> Response.error(null,-2,response.message())
                else -> Response.error(null,response.code(),"fail to connect db")
            }
        } catch (e:Exception){
            e.printStackTrace()
            Response.error(null,-1,e.message.toString())
        }
    }
}