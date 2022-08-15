package com.example.user.data.api.interceptor

import android.util.Log
import com.example.user.data.api.RenewAccessTokenApi
import com.example.user.domain.repository.AuthenticationRepository
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckAccessTokenInterceptor @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var accessToken: String
        runBlocking(Dispatchers.IO) {
            try { accessToken = authenticationRepository.getAccessToken() }
            catch (e: Exception) { throw e }
        }
        Log.e("checking--------+",accessToken)
        var request = chain.request();
        return chain.proceed(request.newBuilder().header("Authorization","Bearer $accessToken").build())
    }
}