package com.example.user.domain.usecase

import android.util.Log
import com.example.user.data.dto.UserDto
import com.example.user.data.model.authentication.ErrorBodyValidateOrRegister
import com.example.user.domain.repository.AuthenticationRepository
import com.example.user.utils.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class SignUpSaveAccountUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(userDto: UserDto): Response<String> {
        return try {
            val response = authenticationRepository.postRequestRegisterSaveAccount(userDto)
            Log.e("6",response.toString())
            when(response.code()){
                201 -> {
                    Log.e("6",response.body().toString())
                    Response.success("Create successfully")
                }
                500 -> {
                    val type = object : TypeToken<ErrorBodyValidateOrRegister>() {}.type
                    val a:ErrorBodyValidateOrRegister = Gson().fromJson(response.errorBody()!!.charStream(), type)
                    Response.error("Create information of account fail",500, a.message)
                }
                else -> Response.error(response.message(),response.code(),response.message())
            }
        } catch (e:Exception) { Response.error(null,-1,e.message.toString())}
    }
}