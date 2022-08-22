package com.example.driver.data.model.booking

import com.example.driver.data.dto.LatLong
import com.example.driver.utils.TypeCar
import com.google.gson.annotations.SerializedName

data class ResponseAcceptBooking(
    val createdAt: String,
    @SerializedName("dropoffCoordinate")
    val destination: LatLong,
    val id: Int,
    val passengerUsername: String,
    val paymentMethod: String,
    @SerializedName("phonenumber")
    val phoneNumber: String,
    @SerializedName("pickupCoordinate")
    val origin: LatLong,
    val price: Double,
    val state: String,
    val typeCar: TypeCar,
    val updatedAt: String
)