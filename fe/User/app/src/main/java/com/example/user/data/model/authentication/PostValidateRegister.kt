package com.example.user.data.model.authentication

import com.google.gson.annotations.SerializedName

data class PostValidateRegister(
    val onceTimePassword: Int,
    @SerializedName("phonenumber")
    val phoneNumber: String
)