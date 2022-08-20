package com.example.user.data.model.booking

import com.example.user.data.dto.LatLong
import com.example.user.utils.PaymentMethod
import com.example.user.utils.TypeCar
import com.google.gson.annotations.SerializedName
import java.util.*

data class ReceiveDriver(
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
