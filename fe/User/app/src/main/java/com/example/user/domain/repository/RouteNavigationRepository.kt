package com.example.user.domain.repository

import com.example.user.data.model.googlemap.PlaceClient
import com.example.user.data.model.googlemap.RouteNavigation
import retrofit2.Response

interface RouteNavigationRepository {
    suspend fun getRouteNavigation(
        origin: String,
        destination: String,
        mode: String
    ): Response<RouteNavigation>

    suspend fun getAddressFromPlaceId(placeId: String): PlaceClient
}