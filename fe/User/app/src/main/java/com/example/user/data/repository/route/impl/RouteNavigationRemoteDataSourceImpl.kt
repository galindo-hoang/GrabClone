package com.example.user.data.repository.route.impl

import com.example.user.BuildConfig
import com.example.user.data.api.RouteNavigationApi
import com.example.user.data.model.googlemap.PlaceClient
import com.example.user.data.model.googlemap.RouteNavigation
import com.example.user.data.repository.route.RouteNavigationRemoteDataSource
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouteNavigationRemoteDataSourceImpl @Inject constructor(
    private val routeNavigationApi: RouteNavigationApi
): RouteNavigationRemoteDataSource {
    override suspend fun getRoutes(
        origin: String,
        destination: String,
        mode: String
    ): Response<RouteNavigation> =
        routeNavigationApi.getRoutes(
            origin,
            destination,
            mode,
            BuildConfig.GOOGLE_MAP_API
        )

    override suspend fun getAddressFromPlaceId(placeId: String): Response<PlaceClient> =
        routeNavigationApi.getAddressFromPlaceId(placeId,BuildConfig.GOOGLE_MAP_API)

}