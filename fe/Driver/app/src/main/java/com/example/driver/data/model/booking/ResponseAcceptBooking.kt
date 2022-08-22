package com.example.driver.data.model.booking

data class ResponseAcceptBooking(
    val createdAt: String,
    val dropoffCoordinate: DropoffCoordinate,
    val id: Int,
    val passengerUsername: String,
    val paymentMethod: String,
    val phonenumber: String,
    val pickupCoordinate: PickupCoordinate,
    val price: Double,
    val state: String,
    val typeCar: String,
    val updatedAt: Any
)