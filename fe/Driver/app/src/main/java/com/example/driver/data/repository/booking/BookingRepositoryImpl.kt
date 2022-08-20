package com.example.driver.data.repository.booking

import com.example.driver.data.dto.BookingDto
import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.dto.UpdateLocation
import com.example.driver.domain.repository.BookingRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepositoryImpl @Inject constructor(
    private val bookingRemoteDataResource: BookingRemoteDataResource
): BookingRepository {
    override suspend fun bookingDriver(bookingDto: BookingDto): Response<Int>{
        try {
            return bookingRemoteDataResource.bookingDriver()
        } catch (e:Exception){
            throw e
        }
    }

    override suspend fun postRegisterFcmToken(registerFCMBody: RegisterFCMBody): Response<Int> {
        return bookingRemoteDataResource.registerFcmToken(registerFCMBody)
    }

    override suspend fun updateCurrentLocation(updateLocation: UpdateLocation): Response<Any> =
        bookingRemoteDataResource.sendCurrentLocation(updateLocation)

    override suspend fun sendAcceptBooking(): Response<Boolean> = bookingRemoteDataResource.sendAcceptBooking()

}

