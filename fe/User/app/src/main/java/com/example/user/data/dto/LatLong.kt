package com.example.user.data.dto

data class LatLong (
    val latitude: Double,
    val longitude: Double
){
    override fun toString(): String {
        return "$longitude,$latitude"
    }
}
