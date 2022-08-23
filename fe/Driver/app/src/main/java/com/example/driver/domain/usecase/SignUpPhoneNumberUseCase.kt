package com.example.driver.domain.usecase

import android.util.Log
import com.example.driver.data.dto.UserDto
import com.example.driver.data.model.authentication.ErrorBodyValidateOrRegister
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.utils.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class SignUpPhoneNumberUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(userDto: UserDto): Response<Int?> {
        return try {
            val response = authenticationRepository.postRequestRegisterPhoneNumber(userDto)
            Log.e("-----",response.body().toString())
            return when(response.code()) {
                200 -> Response.success(response.body()?.otp?.toInt())
                else -> Response.error(null,response.code(),response.message())
            }
        } catch (e:Exception) { Response.error(null,-1,e.message.toString())}
    }
}