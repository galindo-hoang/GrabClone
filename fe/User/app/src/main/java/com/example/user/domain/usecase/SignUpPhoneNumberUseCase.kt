package com.example.user.domain.usecase

import android.util.Log
import com.example.user.data.dto.UserDto
import com.example.user.domain.repository.AuthenticationRepository
import com.example.user.utils.Response
import javax.inject.Inject

class SignUpPhoneNumberUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(userDto: UserDto): Response<Int?> {
        return try {
            val response = authenticationRepository.postRequestRegisterPhoneNumber(userDto)
            Log.e("5",response.toString())
            when(response.code()) {
                200 -> {
                    Log.e("5",response.body().toString())
                    Response.success(response.body()?.otp?.toInt())
                }
//                500 -> {}
                else -> {
//                    val type = object : TypeToken<ErrorBodyValidateOrRegister>() {}.type
//                    Log.e("Error",Gson().fromJson(response.errorBody()!!.charStream(), type))
                    Response.error(null,response.code(),response.message())
                }
            }
        } catch (e:Exception) { Response.error(null,-1,e.message.toString()) }
    }
}