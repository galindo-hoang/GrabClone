package com.example.user.data.api

import retrofit2.Response
import retrofit2.http.GET

interface BookingApi {
    @GET("/api/v1/users/0906892676")
    suspend fun checkk(): Response<Int>
}