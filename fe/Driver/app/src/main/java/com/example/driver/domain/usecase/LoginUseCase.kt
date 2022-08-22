package com.example.driver.domain.usecase

import android.util.Log
import com.example.driver.data.dto.Login
import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.UserDto
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.domain.repository.BookingRepository
import com.example.driver.utils.Response
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import kotlin.coroutines.resumeWithException


class LoginUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val bookingRepository: BookingRepository
) {
    suspend fun invoke(login: Login): Response<UserDto> {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", login.username)
            .addFormDataPart("password", login.password)
            .build()
        return try {
            val response = authenticationRepository.postAccountLogin(requestBody)
            when(response.code()) {
                200 -> {
                    val userDto = authenticationRepository.updateAccount(response.body()!!)
                    val token = FirebaseMessaging.getInstance().token.await()
                    bookingRepository.postRegisterFcmToken(RegisterFCMBody(token,userDto.username!!))
                    Response.success(userDto)
                }
                else -> Response.error(null,response.code(),response.message())
            }
        } catch (e:Exception) { Response.error(null,-1, e.message.toString())}
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun <TResult> Task<TResult>.await(): String {
        return suspendCancellableCoroutine { cont ->
            if(isSuccessful) cont.resume(result.toString(),null)
            else if(isCanceled) cont.resumeWithException(Exception(exception))
        }
    }
}