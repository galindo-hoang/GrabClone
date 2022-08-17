package com.example.driver.data.model.authentication

data class ErrorBodyValidateOrRegister(
    val exception: String,
    val message: String,
    val timestamp: String
)