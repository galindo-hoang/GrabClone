package com.example.user.data.model.route

data class Properties(
    val distance: Int,
    val distance_units: String,
    val mode: String,
    val time: Double,
    val units: String,
    val waypoints: List<Waypoint>
)