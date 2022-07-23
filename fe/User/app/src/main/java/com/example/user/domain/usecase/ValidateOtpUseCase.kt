package com.example.user.domain.usecase

import android.util.Log
import com.example.user.data.dto.ValidateOTP
import com.example.user.data.model.authentication.ErrorBodyValidateOrRegister
import com.example.user.domain.repository.AuthenticationRepository
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