package com.example.driver.data.repository.booking

import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.SubscribeBookingDto
import com.example.driver.data.dto.UpdateLocation
import com.example.driver.domain.repository.BookingRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepositoryImpl @Inject constructor(
    private val bookingRemoteDataResource: BookingRemoteDataResource
): BookingRepository {
    override suspend fun subscribeListenBooking(
        subscribeBookingDto: SubscribeBookingDto
    ): Response<Any> = bookingRemoteDataResource.subscribeListenBooking(subscribeBookingDto)

    override suspend fun unsubscribeListenBooking(
        subscribeBookingDto: SubscribeBookingDto
    ): Response<Any> = bookingRemoteDataResource.subscribeListenBooking(subscribeBookingDto)

    override suspend fun postRegisterFcmToken(
        registerFCMBody: RegisterFCMBody
    ): Response<Int> = bookingRemoteDataResource.registerFcmToken(registerFCMBody)

    override suspend fun updateCurrentLocation(updateLocation: UpdateLocation): Response<Any> =
        bookingRemoteDataResource.sendCurrentLocation(updateLocation)

    override suspend fun sendAcceptBooking(): Response<Boolean> = bookingRemoteDataResource.sendAcceptBooking()

}

