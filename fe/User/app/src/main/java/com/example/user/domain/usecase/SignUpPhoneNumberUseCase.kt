package com.example.user.domain.usecase

import android.util.Log
import com.example.user.data.dto.UserDto
import com.example.user.data.model.authentication.ErrorBodyValidateOrRegister
import com.example.user.domain.repository.AuthenticationRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class SignUpPhoneNumberUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(userDto: UserDto): Int {
        val response = authenticationRepository.postRequestRegisterPhoneNumber(userDto)
        Log.e("-----",response.body().toString())
        return when(response.code()){
            200 -> {
                val body = response.body()
                body?.otp?.toInt() ?: -1
            }
            500 -> {
                val type = object : TypeToken<ErrorBodyValidateOrRegister>() {}.type
                Log.e("Error",Gson().fromJson(response.errorBody()!!.charStream(), type))
                -1
            }
            else -> throw Exception("cant connect to database")
        }
    }
}