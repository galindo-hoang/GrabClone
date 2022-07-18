package com.example.user.data.repository.route

import com.example.user.data.model.googlemap.Route

interface RouteNavigationCacheDataResource {
    fun getRouteNavigationFromCache(): List<Route>
    fun saveRouteNavigationToCache(routes: List<Route>)
}