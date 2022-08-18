package com.example.user.data.dto

data class RegisterFCMBody(
    val fcmToken: String,
    val userId: String
)