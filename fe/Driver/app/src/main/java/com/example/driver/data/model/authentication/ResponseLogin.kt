package com.example.driver.data.model.authentication

import com.google.gson.annotations.SerializedName

data class ResponseLogin(
//    @SerializedName("token")
//    val tokenAuthentication: TokenAuthentication,
    val user: User,
    val accessToken: String,
    val refreshToken: String,
)