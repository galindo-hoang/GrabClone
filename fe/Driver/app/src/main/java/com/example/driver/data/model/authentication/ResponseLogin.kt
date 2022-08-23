package com.example.driver.data.model.authentication

import com.google.gson.annotations.SerializedName

data class ResponseLogin(
//    @SerializedName("token")
//    val tokenAuthentication: TokenAuthentication,
    var user: User,
    @SerializedName("phonenumber")
    val phoneNumber: String,
    val accessToken: String,
    val refreshToken: String,
)