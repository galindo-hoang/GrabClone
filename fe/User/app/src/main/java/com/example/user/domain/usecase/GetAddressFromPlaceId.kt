package com.example.user.domain.usecase

import com.example.user.data.model.googlemap.ResultPlaceClient
import com.example.user.domain.repository.RouteNavigationRepository
import com.example.user.utils.Response
import javax.inject.Inject

class GetAddressFromPlaceId @Inject constructor(
    private val routeNavigationRepository: RouteNavigationRepository
) {
    suspend fun invoke(placeId: String): Response<ResultPlaceClient> {
        val placeClient = routeNavigationRepository.getAddressFromPlaceId(placeId)
        return if(placeClient.result != null && placeClient.status == "200"){
            Response.success(placeClient.result!!)
        }else Response.error(null,placeClient.status)
    }
}