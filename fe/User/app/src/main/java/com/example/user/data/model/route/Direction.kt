package com.example.user.data.model.route

data class Direction(
    val code: String?,
    val routes: List<Route>,
    val uuid: String,
    val message: String,
    val waypoints: List<Waypoint>
)