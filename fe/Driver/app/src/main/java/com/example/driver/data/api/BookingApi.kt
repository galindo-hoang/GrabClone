package com.example.driver.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface BookingApi {
    @GET("/api/v1/users/0906892676")
    suspend fun checkk(): Response<Int>
    @POST("/acceptBooking")
    suspend fun sendAcceptBooking(): Response<Boolean>
}