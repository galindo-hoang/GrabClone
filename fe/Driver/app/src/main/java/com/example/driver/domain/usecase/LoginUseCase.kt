package com.example.driver.domain.usecase

import com.example.driver.data.dto.Login
import com.example.driver.data.dto.UserDto
import com.example.driver.domain.repository.AuthenticationRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class LoginUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun invoke(login: Login): UserDto? {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", login.username)
            .addFormDataPart("password", login.password)
            .build()

        val response = authenticationRepository.postAccountLogin(requestBody)
        return when(response.code()){
            200 -> {
                val body = response.body() ?: throw Exception("Cant get body of response")
                authenticationRepository.updateAccount(body)
            }
            403 -> null
            else -> throw Exception("cant connect to database")
        }
    }
}