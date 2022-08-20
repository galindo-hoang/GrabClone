package com.example.driver.data.dto

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("username")
    val username: String,
    val password: String,
)
