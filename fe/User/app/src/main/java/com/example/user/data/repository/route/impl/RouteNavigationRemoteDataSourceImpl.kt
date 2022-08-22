package com.example.user.data.repository.route.impl

import com.example.user.BuildConfig
import com.example.user.data.api.DirectionApi
import com.example.user.data.api.PlaceApi
import com.example.user.data.model.place.AddressFromText
import com.example.user.data.model.route.Direction
import com.example.user.data.repository.route.RouteNavigationRemoteDataSource
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouteNavigationRemoteDataSourceImpl @Inject constructor(
    private val placeApi: PlaceApi,
    private val directionApi: DirectionApi
): RouteNavigationRemoteDataSource {
    override suspend fun getRoutes(
        method: String,
        origin: String,
        destination: String,
    ): Response<Direction> =
        directionApi.getRoutes(
            latLng = "$origin|$destination",
            mode = method,
            api = BuildConfig.API_ADRRESS
        )

    override suspend fun getAddressFromText(text: String): Response<AddressFromText> =
        placeApi.getAddressFromText(text,BuildConfig.API_ADRRESS)
}