package com.example.user.data.repository.fcm

import com.example.user.data.model.fcm.SubscribeBody
import com.example.user.domain.repository.FCMRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FCMRepositoryImpl @Inject constructor(
    private val fcmRemoteDataResource: FCMRemoteDataResource
): FCMRepository {
//    override suspend fun postSubscribe(subscribeBody: SubscribeBody) {
//        fcmRemoteDataResource.subscribeTopic(subscribeBody)
//    }
//
//    override suspend fun postUnSubscribe(subscribeBody: SubscribeBody) {
//        fcmRemoteDataResource.unsubscribeTopic(subscribeBody)
//    }
}