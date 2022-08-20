package com.example.user.domain.repository

import com.example.user.data.model.place.AddressFromText
import com.example.user.data.model.route.Direction
import retrofit2.Response

interface RouteNavigationRepository {
    suspend fun getRouteNavigation(
        method: String,
        origin: String,
        destination: String
    ): Response<Direction>

    suspend fun getAddressFromText(text: String): Response<AddressFromText>
}