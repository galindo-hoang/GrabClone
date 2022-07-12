package com.example.user.data.model

data class Step(
    val distance: Distance,
    val duration: Duration,
    val end_location: LongLat,
    val html_instructions: String,
    val maneuver: String,
    val polyline: Polyline,
    val start_location: LongLat,
    val travel_mode: String
)