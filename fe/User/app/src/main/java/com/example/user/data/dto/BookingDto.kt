package com.example.user.data.dto

import com.example.user.utils.VehicleType

data class BookingDto (
    val destination: LatLong,
    val source: LatLong,
    val phoneNumber: String,
    val vehicleType: VehicleType
)