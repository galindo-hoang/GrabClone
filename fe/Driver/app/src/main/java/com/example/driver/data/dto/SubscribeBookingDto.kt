package com.example.driver.data.dto

import com.google.gson.annotations.SerializedName

data class SubscribeBookingDto(
    val topicName: String,
    @SerializedName("username")
    val userName: String
)