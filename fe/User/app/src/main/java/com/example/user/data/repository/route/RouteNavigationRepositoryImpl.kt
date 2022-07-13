package com.example.user.data.repository.route

import android.util.Log
import com.example.user.data.model.PlaceClient
import com.example.user.data.model.RouteNavigation
import com.example.user.domain.repository.RouteNavigationRepository
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
    ): RouteNavigation {
        val routeNavigation = RouteNavigation(listOf(),"404")
        try {
            val response = routeNavigationRemoteDataSource.getRoutes(origin, destination, mode)
            routeNavigation.status = response.code().toString()
            if(response.body() != null) routeNavigation.routes = response.body()!!.routes
        } catch (e: Exception){
            e.printStackTrace()
            routeNavigation.status = "500"
        }
        return routeNavigation
    }

    override suspend fun getAddressFromPlaceId(placeId: String): PlaceClient {
        val placeClient = PlaceClient(status = "404")
        try {
            val response = routeNavigationRemoteDataSource.getAddressFromPlaceId(placeId)
            Log.e("2",response.toString())
            placeClient.status = response.code().toString()
            if(response.body() != null) placeClient.result = response.body()!!.result
        } catch (e:Exception){
            e.printStackTrace()
            placeClient.status = "500"
        }
        return placeClient
    }

}