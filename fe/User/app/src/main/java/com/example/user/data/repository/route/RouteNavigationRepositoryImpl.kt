package com.example.user.data.repository.route

import android.util.Log
import com.example.user.data.model.googlemap.PlaceClient
import com.example.user.data.model.googlemap.RouteNavigation
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
        origin: String,
        destination: String,
        mode: String
    ): Response<RouteNavigation> =
        routeNavigationRemoteDataSource.getRoutes(origin, destination, mode)

    override suspend fun getAddressFromPlaceId(placeId: String): PlaceClient {
        val placeClient = PlaceClient(status = "404")
        try {
            val response = routeNavigationRemoteDataSource.getAddressFromPlaceId(placeId)
            placeClient.status = response.code().toString()
            if(response.body() != null) placeClient.result = response.body()!!.result
            else placeClient.status = "400"
        } catch (e:Exception){
            e.printStackTrace()
            placeClient.status = "500"
        }
        return placeClient
    }

}