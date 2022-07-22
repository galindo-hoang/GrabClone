package com.example.user.data.model.authentication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TokenAuthentication(
    @PrimaryKey
    val username: String,
    val accessToken: String,
    val refreshToken: String,
//    val expired: Long
)
