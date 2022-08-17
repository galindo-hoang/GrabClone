package com.example.driver.data.model.authentication

data class BodyValidateOrRegister(
    val message: String,
    val status: String,
    val timestamp: String,
    val otp: String
)