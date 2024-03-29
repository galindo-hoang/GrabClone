package com.example.driver.data.repository.booking

import com.example.driver.data.dto.*
import com.example.driver.data.model.booking.ResponseAcceptBooking
import com.example.driver.data.model.booking.ResponseRegisterFcmToken
import com.example.driver.data.model.booking.ResponseUpdateLocation
import com.example.driver.data.model.fcm.ResponseSubscribe
import com.example.driver.data.model.fcm.ResponseUnSubscribe
import com.example.driver.data.model.route.Direction
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
    ): Response<ResponseSubscribe> = bookingRemoteDataResource.subscribeListenBooking(subscribeBookingDto)

    override suspend fun unsubscribeListenBooking(
        subscribeBookingDto: SubscribeBookingDto
    ): Response<ResponseUnSubscribe> = bookingRemoteDataResource.unsubscribeListenBooking(subscribeBookingDto)

    override suspend fun postRegisterFcmToken(registerFCMBody: RegisterFCMBody): Response<ResponseRegisterFcmToken> =
        bookingRemoteDataResource.registerFcmToken(registerFCMBody)

    override suspend fun updateCurrentLocationBeforeAccept (updateLocation: UpdateLocation): Response<ResponseUpdateLocation> =
        bookingRemoteDataResource.sendCurrentLocationBeforeAccept(updateLocation)

    override suspend fun updateCurrentLocationAfterAccept(updateLocation: UpdateLocation): Response<ResponseUpdateLocation> =
        bookingRemoteDataResource.sendCurrentLocationAfterAccept(updateLocation)

    override suspend fun sendAcceptBooking(
        acceptBooking: AcceptBooking
    ): Response<Void> = bookingRemoteDataResource.sendAcceptBooking(acceptBooking)

    override suspend fun getRouteNavigation (
        method: String, origin: String, destination: String
    ): Response<Direction> = bookingRemoteDataResource.getRoute(method, origin, destination)

    override suspend fun sendFinishRoute (
        finishBooking: FinishBooking
    ): Response<Void> = bookingRemoteDataResource.sendFinishRoute(finishBooking)

}

