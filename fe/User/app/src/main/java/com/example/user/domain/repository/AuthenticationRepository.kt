package com.example.user.domain.repository

import com.example.user.data.dto.UserDto
import com.example.user.data.dto.Login
import com.example.user.data.dto.ValidateOTP
import com.example.user.data.model.authentication.ResponseLogin
import com.example.user.data.model.authentication.SuccessBodyValidateOrRegister
import com.example.user.data.model.authentication.User
import okhttp3.RequestBody
import retrofit2.Response

interface AuthenticationRepository {
    //register
    suspend fun postRequestRegister(userDto: UserDto): Response<SuccessBodyValidateOrRegister>
    suspend fun postValidateRegister(validateOTP: ValidateOTP): Response<SuccessBodyValidateOrRegister>
    // login
    suspend fun updateAccount(responseLogin: ResponseLogin): UserDto
    suspend fun postAccountLogin(requestBody: RequestBody): Response<ResponseLogin>
    // open application did login
    suspend fun getAccount(): UserDto
    // clear
    suspend fun clearAll()


    suspend fun getRefreshToken(): String
    suspend fun getAccessToken(): String
    fun updateAccessToken(accessToken: String): String
}