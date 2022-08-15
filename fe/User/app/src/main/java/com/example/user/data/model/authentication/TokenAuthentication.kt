package com.example.user.data.model.authentication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TokenAuthentication(
    @PrimaryKey
    var username: String,
    var accessToken: String,
    var refreshToken: String,
//    val expired: Long
)
