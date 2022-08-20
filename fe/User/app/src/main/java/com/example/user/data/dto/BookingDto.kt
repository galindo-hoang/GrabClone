package com.example.user.data.dto

import com.example.user.utils.PaymentMethod
import com.example.user.utils.TypeCar
import com.google.gson.annotations.SerializedName

data class BookingDto(
    @SerializedName("dropoffLocation")
    val destination: LatLong,
    val paymentMethod: PaymentMethod,
    @SerializedName("phonenumber")
    var phoneNumber: String = "",
    @SerializedName("pickupLocation")
    val origin: LatLong,
    val price: Double,
    val typeCar: TypeCar,
    var username: String = ""
)