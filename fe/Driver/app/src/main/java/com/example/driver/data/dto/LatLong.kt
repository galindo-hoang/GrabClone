package com.example.driver.data.dto

import com.google.android.gms.maps.model.LatLng

data class LatLong (
    val latitude: Double,
    val longitude: Double
) {
    override fun toString(): String {
        return "$latitude,$longitude"
    }
    fun convertToLatLng(): LatLng = LatLng(latitude,longitude)
}
