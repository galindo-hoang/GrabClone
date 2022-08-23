package com.example.driver.data.model.booking

data class ResponseRegisterFcmToken(
    val createdAt: String,
    val fcmToken: String,
    val id: Int,
    val username: String
)