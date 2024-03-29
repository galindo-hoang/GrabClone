package com.example.driver.domain.usecase

import android.util.Log
import com.example.driver.data.dto.LatLong
import com.example.driver.data.dto.UpdateLocation
import com.example.driver.data.model.booking.ResponseUpdateLocation
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.domain.repository.BookingRepository
import com.example.driver.utils.Response
import javax.inject.Inject

class SendCurrentLocationBeforeAcceptUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val bookingRepository: BookingRepository
) {
    suspend fun invoke(latLong: LatLong): Response<ResponseUpdateLocation> {
        return try {
            val userDto = authenticationRepository.getAccount()
            val response = bookingRepository.updateCurrentLocationBeforeAccept(UpdateLocation(latLong, userDto.username!!))
            Log.e("5",response.toString())
            when(response.code()){
                200 -> {
                    Log.e("5",response.body().toString())
                    Response.success(response.body()!!)
                }
                401 -> Response.error(null,-2,response.message())
                else -> Response.error(null,response.code(),"fail to connect db")
            }

        } catch (e:Exception) {
            Response.error(null,400,e.message.toString())
        }
    }
}