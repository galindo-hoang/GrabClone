package com.example.driver.data.model

import com.example.driver.data.dto.LatLong

data class TempPayloadDataFCM(
    private val src: LatLong,
    private val des: LatLong
)
