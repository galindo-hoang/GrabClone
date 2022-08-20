package com.example.user.domain.usecase

import android.util.Log
import com.example.user.data.dto.ValidateOTP
import com.example.user.data.model.authentication.ErrorBodyValidateOrRegister
import com.example.user.domain.repository.AuthenticationRepository
import com.example.user.utils.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class ValidateOtpUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(validateOTP: ValidateOTP): Response<Int> {
        return try {
            val response = authenticationRepository.postValidateRegister(validateOTP)
            when(response.code()){
                200 -> Response.success(1)
                else -> Response.error(null,response.code(),response.message())
            }
        } catch (e:Exception) { Response.error(-1,-1,e.message.toString()) }
    }
}