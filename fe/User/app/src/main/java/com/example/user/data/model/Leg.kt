package com.example.user.data.model

data class Leg(
    val distance: Distance,
    val duration: Duration,
    val end_address: String,
    val end_location: LongLat,
    val start_address: String,
    val start_location: LongLat,
    val steps: List<Step>,
    val traffic_speed_entry: List<Any>,
    val via_waypoint: List<Any>
)