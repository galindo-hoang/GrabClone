package com.example.driver.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.driver.data.dto.LatLong
import com.example.driver.domain.usecase.AcceptBookingUseCase
import com.example.driver.domain.usecase.DoneDrivingUseCase
import com.example.driver.domain.usecase.GetRouteNavigationUseCase
import com.example.driver.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StimulateViewModel @Inject constructor(
    private val acceptBookingUseCase: AcceptBookingUseCase,
    private val getRouteNavigationUseCase: GetRouteNavigationUseCase,
    private val doneDrivingUseCase: DoneDrivingUseCase
): ViewModel() {
    var origin: LatLong? = LatLong(10.838678,106.665290)
    var destination: LatLong? = LatLong(10.771423,106.698471)
    var afterDoneDriving = false

    fun acceptBooking(id: Int) = liveData {
        emit(Response.loading(null))
        emit(acceptBookingUseCase.invoke(id))
    }

    fun doneDriving() = liveData {
        emit(Response.loading(null))
        var response: Response<String>
        withContext(Dispatchers.IO) { response = doneDrivingUseCase.invoke() }
        emit(response)
    }

    fun getNavigation() = liveData {
        emit(Response.loading(null))
        emit(getRouteNavigationUseCase.invoke(origin.toString(),destination.toString()))
    }
}