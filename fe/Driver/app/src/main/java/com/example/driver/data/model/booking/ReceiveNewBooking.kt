package com.example.driver.data.model.booking

import com.example.driver.data.dto.LatLong
import com.example.driver.utils.PaymentMethod
import com.example.driver.utils.TypeCar
import com.google.gson.annotations.SerializedName
import java.util.*

data class ReceiveNewBooking(
    val bookingId: Int,
    @SerializedName("pickupLocation")
    val origin: LatLong,
    @SerializedName("dropoffLocation")
    val destination: LatLong,
    val typeCar: TypeCar,
    val price: Double,
    val paymentMethod: PaymentMethod,
    val createAt: Date,
    val startTime: Date,
    val rideId: Int
)
