package com.example.driver.data.dto

import com.google.gson.annotations.SerializedName

data class ValidateOTP(
    val onceTimePassword: Int,
    @SerializedName("phonenumber")
    val phoneNumber: String
)