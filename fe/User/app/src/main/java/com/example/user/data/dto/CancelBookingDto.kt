package com.example.user.data.dto

import com.google.gson.annotations.SerializedName

data class CancelBookingDto(
    @SerializedName("username")
    val userName: String
)
