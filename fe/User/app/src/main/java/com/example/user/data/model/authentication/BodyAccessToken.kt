package com.example.user.data.model.authentication

data class BodyAccessToken(
    val exp: Long,
    val iss: String,
    val roles: List<String>,
    val sub: String
)