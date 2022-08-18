package com.example.user.data.dto

data class BookingDto(
    val dropoffLocation: DropoffLocation,
    val paymentMethod: String,
    val phonenumber: String,
    val pickupLocation: PickupLocation,
    val price: Int,
    val typeCar: String,
    val username: String
)