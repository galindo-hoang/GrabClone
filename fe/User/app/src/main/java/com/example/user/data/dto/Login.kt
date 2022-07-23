package com.example.user.data.dto

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("username")
    val username: String,
    val password: String,
)
