package com.example.user.data.repository.route.impl

import com.example.user.data.model.Route
import com.example.user.data.repository.route.RouteNavigationCacheDataResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouteNavigationCacheDataResourceImpl @Inject constructor(): RouteNavigationCacheDataResource {
    private val routeList = mutableListOf<Route>()
    override fun getRouteNavigationFromCache(): List<Route> = routeList

    override fun saveRouteNavigationToCache(routes: List<Route>) {
        routeList.clear()
        routeList.addAll(routes)
    }
}