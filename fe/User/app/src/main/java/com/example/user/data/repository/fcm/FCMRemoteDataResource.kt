package com.example.user.data.repository.fcm

import com.example.user.data.model.fcm.SubscribeBody

interface FCMRemoteDataResource {
    suspend fun subscribeTopic(subscribeBody: SubscribeBody)
    suspend fun unsubscribeTopic(subscribeBody: SubscribeBody)
}