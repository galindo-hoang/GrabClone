package com.example.user.domain.usecase

import android.util.Log
import com.example.user.data.dto.BookingDto
import com.example.user.data.model.authentication.ErrorBodyValidateOrRegister
import com.example.user.domain.repository.BookingRepository
import com.example.user.utils.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import javax.inject.Inject

class BookingCarUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend fun invoke(bookingDto: BookingDto): Response<Int> {
        val response = bookingRepository.bookingDriver(bookingDto)
        return when(response.code()){
            200 -> {
                if(response.body() != null) Response.success(response.body()!!)
                else Response.error(null,"Cant save data")
            }
            401 -> {
                Log.e("+++++++++",response.message())
                Response.error(-2,response.message())
            }
            500 -> {
                val type = object : TypeToken<ErrorBodyValidateOrRegister>() {}.type
                val a: ErrorBodyValidateOrRegister = Gson().fromJson(response.errorBody()!!.charStream(), type)
                Response.error(-1,a.message)
            }
            else -> Response.error(null,"fail to connect db")
        }
    }
}