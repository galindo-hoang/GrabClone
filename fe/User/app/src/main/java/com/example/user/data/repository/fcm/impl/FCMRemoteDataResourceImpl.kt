package com.example.user.data.repository.fcm.impl

import com.example.user.data.api.FCMApi
import com.example.user.data.model.fcm.SubscribeBody
import com.example.user.data.repository.fcm.FCMRemoteDataResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FCMRemoteDataResourceImpl @Inject constructor(
    private val fcmApi: FCMApi
) : FCMRemoteDataResource {
//    override suspend fun subscribeTopic(subscribeBody: SubscribeBody) =
//        fcmApi.subscribeTopic(subscribeBody)
//
//    override suspend fun unsubscribeTopic(subscribeBody: SubscribeBody) =
//        fcmApi.unsubscribeTopic(subscribeBody)
}