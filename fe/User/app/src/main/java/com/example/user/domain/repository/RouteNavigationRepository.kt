package com.example.user.domain.repository

import com.example.user.data.model.PlaceClient
import com.example.user.data.model.RouteNavigation
import retrofit2.Response

interface RouteNavigationRepository {
    suspend fun getRouteNavigation(
        origin: String,
        destination: String,
        mode: String
    ): RouteNavigation

    suspend fun getAddressFromPlaceId(placeId: String): PlaceClient
}