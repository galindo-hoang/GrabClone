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
        val response = authenticationRepository.postRequestRegisterSaveAccount(userDto)
        return when(response.code()){
            201 -> {
                val body = response.body()
                if(body != null) Response.success("Create successfully")
                else Response.error("Cant save data","FAIL")
            }
            500 -> {
                val type = object : TypeToken<ErrorBodyValidateOrRegister>() {}.type
                val a:ErrorBodyValidateOrRegister = Gson().fromJson(response.errorBody()!!.charStream(), type)
                Response.error("Create information of account fail",a.message)
            }
            else -> throw Exception("cant connect to database")
        }
    }
}