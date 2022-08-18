package com.example.driver.domain.usecase

import android.util.Log
import com.example.driver.data.dto.Login
import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.UserDto
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.domain.repository.BookingRepository
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
                val userDto = authenticationRepository.updateAccount(body)
                try {
                    val token = FirebaseMessaging.getInstance().token.await()
                    bookingRepository.postRegisterFcmToken(RegisterFCMBody(token, userDto.username!!))
                } catch (e:Exception) { throw e }
                userDto
            }
            403 -> null
            else -> throw Exception("cant connect to database")
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun <TResult> Task<TResult>.await(): String {
        return suspendCancellableCoroutine { cont ->
            if(isSuccessful) cont.resume(result.toString(),null)
            else if(isCanceled) cont.resumeWithException(Exception(exception))
        }
    }
}