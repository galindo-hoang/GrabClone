package com.example.user.data.repository.booking

import com.example.user.data.dto.BookingDto
import com.example.user.data.dto.CancelBookingDto
import com.example.user.data.dto.RegisterFCMBody
import com.example.user.data.model.booking.ResponseBooking
import com.example.user.data.model.fcm.ResponseRegisterFcmToken
import com.example.user.domain.repository.BookingRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class BookingRepositoryImpl @Inject constructor(
    private val bookingRemoteDataResource: BookingRemoteDataResource
): BookingRepository {
    override suspend fun bookingDriver(bookingDto: BookingDto): Response<ResponseBooking> =
        bookingRemoteDataResource.bookingDriver(bookingDto)

    override suspend fun cancelBookingDriver(cancelBookingDto: CancelBookingDto): Response<ResponseBooking> =
        bookingRemoteDataResource.cancelBookingDriver(cancelBookingDto)

    override suspend fun postRegisterFcmToken(registerFCMBody: RegisterFCMBody): Response<ResponseRegisterFcmToken> {
        return bookingRemoteDataResource.registerFcmToken(registerFCMBody)
    }

}

