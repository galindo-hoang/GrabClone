package com.example.user.data.api

import com.example.user.data.dto.LatLong
import com.example.user.data.model.route.Direction
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DirectionApi {
    @GET("/directions/v5/mapbox/{method}/{src}/{des}")
    fun getRoutes(
        @Path("method") method: String,
        @Path("src") origin: String,
        @Path("des") destination: String,
        @Query("geometries") geometry: String = "geojson",
        @Query("access_token") token: String,
    ): Response<Direction>
}