package com.example.user.data.api

import android.util.Log
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import javax.inject.Inject

class Check @Inject constructor(
    private val routeNavigationApi: RouteNavigationApi,
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking{
        var request = chain.request();
        var iss = false
        val a = withContext(Dispatchers.IO) {
            routeNavigationApi.getAddressFromPlaceId("1","2")
        }
        Log.e("-----",a.toString())
        return@runBlocking chain.proceed(
            request = request.newBuilder()
                .header("phonenumber","123")
                .header("onceTimePassword","qwewqe")
                .build())
    }
}