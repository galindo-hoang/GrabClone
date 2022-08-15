package com.example.user.data.api

import retrofit2.Response
import retrofit2.http.GET

interface BookingApi {
    @GET("/api/v1/users/0833759409")
    suspend fun checkk(): Response<Int>
}