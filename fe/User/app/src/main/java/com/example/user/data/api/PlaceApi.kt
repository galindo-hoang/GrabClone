package com.example.user.data.api

import com.example.user.data.model.place.AddressFromText
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceApi {
    @GET("/v1/geocode/autocomplete")
    suspend fun getAddressFromText(
        @Query("text") text: String,
        @Query("apiKey") api: String
    ): Response<AddressFromText>
}