package com.example.user.data.model.googlemap

data class Route(
    val legs: List<Leg>,
//    val overview_polyline: OverviewPolyline,
    val summary: String
)