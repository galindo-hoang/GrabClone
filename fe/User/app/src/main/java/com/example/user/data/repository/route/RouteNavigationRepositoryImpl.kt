package com.example.user.data.repository.route

import com.example.user.data.model.place.AddressFromText
import com.example.user.data.model.route.Direction
import com.example.user.domain.repository.RouteNavigationRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouteNavigationRepositoryImpl @Inject constructor(
    private val routeNavigationCacheDataResource: RouteNavigationCacheDataResource,
    private val routeNavigationRemoteDataSource: RouteNavigationRemoteDataSource,
): RouteNavigationRepository {
    override suspend fun getRouteNavigation(
        method: String,
        origin: String,
        destination: String
    ): Response<Direction> = routeNavigationRemoteDataSource.getRoutes(method, origin, destination)

    override suspend fun getAddressFromText(text: String): Response<AddressFromText> =
        routeNavigationRemoteDataSource.getAddressFromText(text)
}