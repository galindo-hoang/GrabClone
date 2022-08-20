package com.example.driver.data.dto

import com.google.gson.annotations.SerializedName

data class SubscribeBookingDto(
    val topicName: String,
    @SerializedName("userName")
    val username: String
)