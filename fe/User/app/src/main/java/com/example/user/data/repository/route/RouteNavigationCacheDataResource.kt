package com.example.user.data.repository.route

import com.example.user.data.model.Route
import com.example.user.data.model.RouteNavigation

interface RouteNavigationCacheDataResource {
    fun getRouteNavigationFromCache(): List<Route>
    fun saveRouteNavigationToCache(routes: List<Route>)
}