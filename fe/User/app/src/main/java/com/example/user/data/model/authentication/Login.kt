package com.example.user.data.model.authentication

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("token")
    val tokenAuthentication: TokenAuthentication,
    val user: User
)