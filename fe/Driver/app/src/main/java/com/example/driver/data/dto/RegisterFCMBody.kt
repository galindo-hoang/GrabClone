package com.example.driver.data.dto

data class RegisterFCMBody(
    val fcmToken: String,
    val userId: String
)