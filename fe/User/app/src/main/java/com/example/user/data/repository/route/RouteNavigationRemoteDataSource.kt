package com.example.user.data.repository.route

import com.example.user.data.model.RouteNavigation
import retrofit2.Response

interface RouteNavigationRemoteDataSource {
    suspend fun getRoutes(
        origin: String,
        destination: String,
        mode: String): Response<RouteNavigation>
}