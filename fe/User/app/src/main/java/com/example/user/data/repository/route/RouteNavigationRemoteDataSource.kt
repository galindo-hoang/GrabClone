package com.example.user.data.repository.route

import com.example.user.data.model.place.AddressFromText
import com.example.user.data.model.route.Direction
import retrofit2.Response

interface RouteNavigationRemoteDataSource {
    suspend fun getRoutes(
        method: String,
        origin: String,
        destination: String
    ): Response<Direction>

    suspend fun getAddressFromText(text: String): Response<AddressFromText>
}