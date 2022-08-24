package com.example.user.domain.usecase

import android.util.Log
import com.example.user.data.dto.ValidateOTP
import com.example.user.data.model.authentication.BodyValidateOrRegister
import com.example.user.domain.repository.AuthenticationRepository
import com.example.user.utils.Response
import javax.inject.Inject

class ValidateOtpUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(validateOTP: ValidateOTP): Response<BodyValidateOrRegister> {
        return try {
            val response = authenticationRepository.postValidateRegister(validateOTP)
            Log.e("7",response.toString())
            when(response.code()){
                200 -> {
                    Log.e("7",response.body().toString())
                    Response.success(response.body()!!)
                }
                else -> Response.error(null,response.code(),response.message())
            }
        } catch (e:Exception) { Response.error(null,-1,e.message.toString()) }
    }
}