package com.example.driver.data.api

import com.example.driver.data.model.route.Direction
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionApi {
    @GET("/v1/routing")
    suspend fun getRoutes(
        @Query("waypoints") latLng: String,
        @Query("mode") mode:String,
        @Query("apiKey") api:String,
    ): Response<Direction>
}