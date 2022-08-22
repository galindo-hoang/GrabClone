package com.example.driver.data.dto

data class LatLong (
    val latitude: Double,
    val longitude: Double
) {
    override fun toString(): String {
        return "$latitude,$longitude"
    }
}
