package com.example.user.data.model.googlemap

data class LongLat(
    val lat: Double,
    val lng: Double
) {
    override fun toString(): String {
        return "$lat,$lng"
    }
}