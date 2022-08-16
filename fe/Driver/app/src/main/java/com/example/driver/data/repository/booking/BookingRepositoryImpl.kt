package com.example.driver.data.repository.booking

import com.example.driver.data.dto.BookingDto
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

}
