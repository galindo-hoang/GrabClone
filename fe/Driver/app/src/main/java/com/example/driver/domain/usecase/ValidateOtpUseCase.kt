package com.example.driver.domain.usecase

import android.util.Log
import com.example.driver.data.dto.ValidateOTP
import com.example.driver.data.model.authentication.ErrorBodyValidateOrRegister
import com.example.driver.domain.repository.AuthenticationRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class ValidateOtpUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(validateOTP: ValidateOTP): Int{
        val response = authenticationRepository.postValidateRegister(validateOTP)
        return when(response.code()){
            200 -> 1
            500 -> {
                val type = object : TypeToken<ErrorBodyValidateOrRegister>() {}.type
                Log.e("Error",Gson().fromJson(response.errorBody()!!.charStream(), type))
                0
            }
            else -> throw Exception("cant connect to database")
        }
    }
}