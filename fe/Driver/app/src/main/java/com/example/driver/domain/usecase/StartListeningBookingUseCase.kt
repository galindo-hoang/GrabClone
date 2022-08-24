package com.example.driver.domain.usecase

import android.util.Log
import com.example.driver.data.dto.SubscribeBookingDto
import com.example.driver.data.model.fcm.ResponseSubscribe
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.domain.repository.BookingRepository
import com.example.driver.utils.Response
import javax.inject.Inject

class StartListeningBookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(): Response<ResponseSubscribe> {
        return try {
            val userDto = authenticationRepository.getAccount()
            Log.e("--------",userDto.toString())
            val response = bookingRepository.subscribeListenBooking(SubscribeBookingDto("booking", userDto.username!!))
            Log.e("--------",response.toString())
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