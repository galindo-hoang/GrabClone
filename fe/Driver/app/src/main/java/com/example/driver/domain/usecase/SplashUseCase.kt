package com.example.driver.domain.usecase

import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.domain.repository.BookingRepository
import com.example.driver.utils.Response
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class SplashUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val bookingRepository: BookingRepository
) {
    suspend fun invoke(): Response<Int> {
        val tokenSize = authenticationRepository.getNumberToken()
        val accountSize = authenticationRepository.getNumberAccount()
        return try {
            if (tokenSize == 1 && accountSize == 1) {
                val token = FirebaseMessaging.getInstance().token.await()
                val userDto = authenticationRepository.getAccount()
                bookingRepository.postRegisterFcmToken(RegisterFCMBody(token, userDto.username!!))
                Response.success(1)
            }
            else Response.success(-1)
        } catch (e: Exception) { Response.success(-1) }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun <TResult> Task<TResult>.await(): String {
        return suspendCancellableCoroutine { cont ->
            if(isSuccessful) cont.resume(result.toString(),null)
            else if(isCanceled) cont.resumeWithException(Exception(exception))
        }
    }
}