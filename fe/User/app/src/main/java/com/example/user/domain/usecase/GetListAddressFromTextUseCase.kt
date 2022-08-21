package com.example.user.domain.usecase

import com.example.user.data.model.place.AddressFromText
import com.example.user.domain.repository.BookingRepository
import com.example.user.domain.repository.RouteNavigationRepository
import com.example.user.utils.Response
import javax.inject.Inject

class GetListAddressFromTextUseCase @Inject constructor(
    private val routeNavigationRepository: RouteNavigationRepository
) {
    suspend fun invoke(text: String): Response<AddressFromText> {
        return try {
            val response = routeNavigationRepository.getAddressFromText(text)
            when (response.code()) {
                200 -> Response.success(response.body()!!)
                else -> Response.error(null,response.code(),response.message())
            }
        } catch (e:Exception) { Response.error(null,-1,e.message.toString()) }
    }
}