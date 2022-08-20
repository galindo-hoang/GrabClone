package com.example.user.data.dto

import com.example.user.utils.TypeCar
import com.google.gson.annotations.SerializedName

data class BookingDto(
    @SerializedName("dropoffLocation")
    val destination: LatLong,
    val paymentMethod: String,
    @SerializedName("phonenumber")
    val phoneNumber: String,
    @SerializedName("pickupLocation")
    val origin: LatLong,
    val price: Double,
    val typeCar: TypeCar,
    val username: String
)