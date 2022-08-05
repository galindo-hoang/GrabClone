package com.example.user.domain.repository

import com.example.user.data.model.fcm.SubscribeBody

interface FCMRepository {
    suspend fun postSubscribe(subscribeBody: SubscribeBody)
    suspend fun postUnSubscribe(subscribeBody: SubscribeBody)
}