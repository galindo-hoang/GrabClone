package com.example.user.data.model.place

import com.example.user.data.dto.LatLong

data class Address(
    val position: LatLong,
    val name: String
)
