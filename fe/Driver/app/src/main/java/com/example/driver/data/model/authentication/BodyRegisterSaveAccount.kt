package com.example.driver.data.model.authentication

import com.google.gson.annotations.SerializedName

data class BodyRegisterSaveAccount(
    val id: Int,
    val otp: String,
    val password: Any,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    val roles: List<String>,
    val username: String,
    val version: Int
)