package com.example.driver.data.dto

import com.google.gson.annotations.SerializedName

data class RegisterFCMBody(
    val fcmToken: String,
    @SerializedName("username")
    val userName: String
)