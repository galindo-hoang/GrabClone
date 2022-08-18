package com.example.driver.data.model.authentication

data class BodyRefreshToken(
    val exp: Long,
    val iss: String,
    val sub: String
)