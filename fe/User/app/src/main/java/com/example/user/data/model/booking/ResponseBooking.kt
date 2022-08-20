package com.example.user.data.model.booking

import com.example.user.data.dto.LatLong
import com.example.user.utils.BookingState
import com.example.user.utils.PaymentMethod
import com.example.user.utils.TypeCar
import com.google.gson.annotations.SerializedName

data class ResponseBooking(
    val createdAt: String,
    @SerializedName("dropoffCoordinate")
    val destination: LatLong,
    val passengerUsername: String,
    val paymentMethod: PaymentMethod,
    @SerializedName("phonenumber")
    val phoneNumber: String,
    @SerializedName("pickupCoordinate")
    val origin: LatLong,
    val price: Double,
    val state: BookingState,
    val typeCar: TypeCar,
    val updatedAt: Any
)