package com.example.user.domain.repository

import com.example.user.data.dto.UserDto
import com.example.user.data.dto.ValidateOTP
import com.example.user.data.model.authentication.BodyRefreshToken
import com.example.user.data.model.authentication.BodyRegisterSaveAccount
import com.example.user.data.model.authentication.ResponseLogin
import com.example.user.data.model.authentication.BodyValidateOrRegister
import okhttp3.RequestBody
import retrofit2.Response

interface AuthenticationRepository {
    //register
    suspend fun postRequestRegisterPhoneNumber(userDto: UserDto): Response<BodyValidateOrRegister>
    suspend fun postRequestRegisterSaveAccount(userDto: UserDto): Response<BodyRegisterSaveAccount>
    suspend fun postValidateRegister(validateOTP: ValidateOTP): Response<BodyValidateOrRegister>
    // login
    fun updateAccount(responseLogin: ResponseLogin): UserDto
    suspend fun postAccountLogin(requestBody: RequestBody): Response<ResponseLogin>
    // after login
    suspend fun getAccount(): UserDto
    // clear
    suspend fun clearAll()


//    suspend fun getRefreshToken(): String
    suspend fun getAccessToken(): String
}