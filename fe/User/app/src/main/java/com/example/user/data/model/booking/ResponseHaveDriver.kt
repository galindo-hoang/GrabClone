package com.example.user.data.model.booking

import com.example.user.data.dto.LatLong
import com.example.user.utils.PaymentMethod
import com.example.user.utils.TypeCar
import com.google.gson.annotations.SerializedName
import java.util.*

data class ResponseHaveDriver(
    val body: String,
    val content: String,
    val createdAt: Date,
    @SerializedName("dropoffLocation")
    val destination: LatLong,
    val paymentMethod: PaymentMethod,
    @SerializedName("pickupLocation")
    val origin: LatLong,
    val price: Double,
    val rideId: Int,
    val startTime: Date,
    val typeCar: TypeCar
)