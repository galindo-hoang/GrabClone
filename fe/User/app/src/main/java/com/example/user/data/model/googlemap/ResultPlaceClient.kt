package com.example.user.data.model.googlemap

data class ResultPlaceClient(
    val formatted_address: String,
    val geometry: Geometry,
    val place_id: String
)