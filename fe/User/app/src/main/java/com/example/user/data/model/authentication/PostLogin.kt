package com.example.user.data.model.authentication

import com.google.gson.annotations.SerializedName

data class PostLogin(
    @SerializedName("username")
    val userName: String,
    val password: String,
)
