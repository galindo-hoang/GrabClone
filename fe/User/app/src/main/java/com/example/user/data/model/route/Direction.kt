package com.example.user.data.model.route

data class Direction(
    val features: List<Feature>,
    val statusCode: Int?,
    val error: String?,
    val message: String?
)