package com.example.driver.data.api.interceptor

import android.util.Log
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.exception.ExpiredRefreshTokenExceptionCustom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckAccessTokenInterceptor @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request();
        try {
            var accessToken: String
            runBlocking(Dispatchers.IO) {
                accessToken = authenticationRepository.getAccessToken()
            }
            return chain.proceed(request.newBuilder().header("Authorization","Bearer $accessToken").build())
        }
        catch (e: Exception) {
            Log.e("==============","hello")
            return when (e){
                is ExpiredRefreshTokenExceptionCustom -> {
                    Response.Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_1)
                        .code(401)
                        .message(e.message.toString())
                        .build()
                }
                else ->
                    Response.Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_1)
                        .code(400)
                        .message(e.message.toString())
                        .build()
            }
        }
    }
}