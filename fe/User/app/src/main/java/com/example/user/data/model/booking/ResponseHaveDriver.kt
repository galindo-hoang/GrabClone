package com.example.user.data.model.booking

data class ResponseHaveDriver(
    val body: String,
    val bookingId: Int,
    val content: String,
    val createdAt: String,
    val dropoffLocation: DropoffLocation,
    val paymentMethod: String,
    val pickupLocation: PickupLocation,
    val price: Double,
    val rideId: Int,
    val startTime: String,
    val typeCar: String
)