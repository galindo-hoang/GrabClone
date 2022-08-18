package com.example.driver.domain.usecase

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
        val response = authenticationRepository.postRequestRegisterSaveAccount(userDto)
        return when(response.code()){
            201 -> {
                val body = response.body()
                if(body != null) Response.success("Create successfully")
                else Response.error("Cant save data",response.code(),"FAIL")
            }
            500 -> {
                val type = object : TypeToken<ErrorBodyValidateOrRegister>() {}.type
                val a:ErrorBodyValidateOrRegister = Gson().fromJson(response.errorBody()!!.charStream(), type)
                Response.error("Create information of account fail",response.code(),a.message)
            }
            else -> throw Exception("cant connect to database")
        }
    }
}