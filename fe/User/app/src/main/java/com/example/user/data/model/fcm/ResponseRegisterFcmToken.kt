package com.example.user.data.model.fcm

data class ResponseRegisterFcmToken(
    val createdAt: String,
    val fcmToken: String,
    val id: Int,
    val username: String
)