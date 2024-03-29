package com.example.driver.domain.usecase

import android.util.Log
import com.example.driver.data.dto.ValidateOTP
import com.example.driver.data.model.authentication.BodyValidateOrRegister
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.utils.Response
import javax.inject.Inject

class ValidateOtpUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(validateOTP: ValidateOTP): Response<BodyValidateOrRegister>{
        return try {
            val response = authenticationRepository.postValidateRegister(validateOTP)
            Log.e("10",response.toString())
            when(response.code()){
                200 -> {
                    Log.e("10",response.body().toString())
                    Response.success(response.body()!!)
                }
                else -> Response.error(null,response.code(),response.message())
            }
        } catch (e:Exception) { Response.error(null,-1,e.message.toString()) }
    }
}