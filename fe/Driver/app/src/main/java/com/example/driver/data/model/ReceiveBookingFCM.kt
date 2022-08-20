package com.example.driver.data.model

import com.example.driver.data.dto.LatLong
import com.example.driver.utils.PaymentMethod
import com.example.driver.utils.TypeCar
import com.google.gson.annotations.SerializedName
import java.util.*

data class ReceiveBookingFCM(
    private val bookingId: Int,
    @SerializedName("pickupLocation")
    private val origin: LatLong,
    @SerializedName("dropoffLocation")
    private val destination: LatLong,
    private val typeCar: TypeCar,
    private val price: Double,
    private val paymentMethod: PaymentMethod,
    private val createAt: Date,
    private val startTime: Date,
    private val rideId: Int
)
