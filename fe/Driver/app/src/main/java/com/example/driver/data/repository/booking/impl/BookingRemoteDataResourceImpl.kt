package com.example.driver.data.repository.booking.impl

import com.example.driver.BuildConfig
import com.example.driver.data.api.BookingApi
import com.example.driver.data.api.DirectionApi
import com.example.driver.data.api.FCMApi
import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.SubscribeBookingDto
import com.example.driver.data.dto.UpdateLocation
import com.example.driver.data.model.route.Direction
import com.example.driver.data.repository.booking.BookingRemoteDataResource
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRemoteDataResourceImpl @Inject constructor(
    private val bookingApi: BookingApi,
    private val fcmApi: FCMApi,
    private val directionApi: DirectionApi
): BookingRemoteDataResource {
    override suspend fun subscribeListenBooking(
        subscribeBookingDto: SubscribeBookingDto
    ): Response<Any> = fcmApi.subscribeListeningBooking(subscribeBookingDto)

    override suspend fun unsubscribeListenBooking(
        subscribeBookingDto: SubscribeBookingDto
    ): Response<Any> = fcmApi.unsubscribeListeningBooking(subscribeBookingDto)

    override suspend fun registerFcmToken(registerFCMBody: RegisterFCMBody): Response<Int> =
        fcmApi.registerToken(registerFCMBody)

    override suspend fun sendCurrentLocation(updateLocation: UpdateLocation): Response<Any> =
        fcmApi.sendCurrentLocation(updateLocation)

    override suspend fun sendAcceptBooking(): Response<Boolean> = bookingApi.sendAcceptBooking()
    override suspend fun getRoute(
        method: String,
        origin: String,
        destination: String
    ): Response<Direction> =
        directionApi.getRoutes(
            latLng = "$origin|$destination",
            mode = method,
            api = BuildConfig.API_ADRRESS
        )

}