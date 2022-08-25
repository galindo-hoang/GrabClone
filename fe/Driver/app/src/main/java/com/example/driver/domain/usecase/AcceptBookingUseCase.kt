package com.example.driver.domain.usecase

import android.util.Log
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
    suspend fun invoke(id: Int): Response<String>{
        return try {
            val response = bookingRepository.sendAcceptBooking(AcceptBooking(id, authenticationRepository.getAccount().username.toString()))
            Log.e("1",response.toString())
            when(response.code()){
                200 -> {
                    Log.e("1",response.body().toString())
                    Response.success(response.body().toString())
                }
                401 -> { Response.error(null,-2,response.message()) }
                else -> Response.error(null,response.code(),response.message())
            }
        } catch (e:Exception) { Response.error(null,-1,e.message.toString())}
    }
}