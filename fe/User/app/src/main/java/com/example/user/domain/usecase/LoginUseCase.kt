package com.example.user.domain.usecase

import android.util.Log
import com.example.user.data.dto.Login
import com.example.user.data.dto.RegisterFCMBody
import com.example.user.data.dto.UserDto
import com.example.user.domain.repository.AuthenticationRepository
import com.example.user.domain.repository.BookingRepository
import com.example.user.utils.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class LoginUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val bookingRepository: BookingRepository
) {
    suspend fun invoke(token: String, login: Login): Response<UserDto> {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", login.username)
            .addFormDataPart("password", login.password)
            .build()
        return try {
            val response = authenticationRepository.postAccountLogin(requestBody)
            Log.e("4",response.toString())
            when(response.code()) {
                200 -> {
                    Log.e("4",response.body().toString())
                    Log.e("4",response.body().toString())
                    response.body()!!.user.phoneNumber = response.body()!!.phoneNumber
                    val userDto = authenticationRepository.updateAccount(response.body()!!)
                    val responseFcm = bookingRepository.postRegisterFcmToken(RegisterFCMBody(token, userDto.username!!))
                    Log.e("4",responseFcm.toString())
                    when(responseFcm.code()) {
                        200 -> {
                            Log.e("4",responseFcm.body().toString())
                            Response.success(userDto)
                        }
                        else -> Response.error(null,responseFcm.code(),responseFcm.message())
                    }
                }
                else -> Response.error(null,response.code(),response.message())
            }
        } catch (e:Exception) { Response.error(null,-1,e.message.toString()) }
    }
}