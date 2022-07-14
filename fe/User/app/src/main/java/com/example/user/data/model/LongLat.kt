package com.example.user.data.model

data class LongLat(
    val lat: Double,
    val lng: Double
) {
    override fun toString(): String {
        return "$lat,$lng"
    }
}