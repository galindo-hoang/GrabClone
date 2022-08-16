package com.example.driver.data.model.authentication

data class BodyAccessToken(
    val exp: Long,
    val iss: String,
    val roles: List<String>,
    val sub: String
)