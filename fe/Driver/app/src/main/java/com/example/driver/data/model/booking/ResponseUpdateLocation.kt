package com.example.driver.data.model.booking

import com.example.driver.data.dto.LatLong

data class ResponseUpdateLocation(
    val countEvalute: Any,
    val countRide: Any,
    val driverUsername: String,
    val id: Int,
    val location: LatLong,
    val locationUpdateTime: String,
    val phoneNumber: String,
    val starEvalute: Any
)