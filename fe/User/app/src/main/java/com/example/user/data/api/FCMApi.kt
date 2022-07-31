package com.example.user.data.api

import com.example.user.data.model.fcm.SubscribeBody
import retrofit2.http.Body
import retrofit2.http.POST

interface FCMApi {
    @POST("/notification/subscribe")
    suspend fun subscribeTopic(@Body subscribeBody: SubscribeBody)

    @POST("/notification/unsubscribe")
    suspend fun unsubscribeTopic(@Body subscribeBody: SubscribeBody)
}