package com.example.driver.domain.usecase

import android.util.Log
import com.example.driver.data.dto.UserDto
import com.example.driver.data.model.authentication.ErrorBodyValidateOrRegister
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.utils.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class SignUpSaveAccountUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(userDto: UserDto): Response<String> {
        Log.e("=====",userDto.toString())
        return try {
            val response = authenticationRepository.postRequestRegisterSaveAccount(userDto)
            Log.e("7",response.toString())
            return when(response.code()){
                201 -> {
                    Log.e("7",response.body().toString())
                    Response.success("Create successfully")
                }
                500 -> {
                    val type = object : TypeToken<ErrorBodyValidateOrRegister>() {}.type
                    val a:ErrorBodyValidateOrRegister = Gson().fromJson(response.errorBody()!!.charStream(), type)
                    Response.error("Create information of account fail",response.code(),a.message)
                }
                else -> Response.error(response.message(),response.code(),response.message())
            }
        } catch (e:Exception) { Response.error(null,-1,e.message.toString()) }
    }
}