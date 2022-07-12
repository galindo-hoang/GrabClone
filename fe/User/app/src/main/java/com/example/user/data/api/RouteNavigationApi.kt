package com.example.user.data.api

import com.example.user.data.model.RouteNavigation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RouteNavigationApi {
    @GET("/maps/api/directions/json")
    suspend fun getRoutes(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("key") key: String,
    ): Response<RouteNavigation>
}