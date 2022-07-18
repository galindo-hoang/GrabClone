package com.example.user.domain.repository

import com.example.user.data.model.googlemap.PlaceClient
import com.example.user.data.model.googlemap.RouteNavigation

interface RouteNavigationRepository {
    suspend fun getRouteNavigation(
        origin: String,
        destination: String,
        mode: String
    ): RouteNavigation

    suspend fun getAddressFromPlaceId(placeId: String): PlaceClient
}