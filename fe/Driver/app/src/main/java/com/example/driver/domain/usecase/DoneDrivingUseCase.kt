package com.example.driver.domain.usecase

import android.util.Log
import com.example.driver.data.dto.FinishBooking
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.domain.repository.BookingRepository
import com.example.driver.utils.Response
import javax.inject.Inject

class DoneDrivingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(): Response<String> {
        return try {
            val userDto = authenticationRepository.getAccount()
            val response = bookingRepository.sendFinishRoute(FinishBooking(userDto.username!!))
            Log.e("2",response.toString())
            when (response.code()) {
                200 -> {
                    Log.e("2",response.body().toString())
                    Response.success(response.body().toString())
                }
                401 -> Response.error(null,-2,response.message())
                else -> Response.error(null,response.code(),response.message())
            }
        } catch (e:Exception) { Response.error(null,-1,e.message.toString())}
    }
}