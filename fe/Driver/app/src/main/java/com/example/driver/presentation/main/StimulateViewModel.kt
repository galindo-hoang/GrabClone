package com.example.driver.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.driver.data.dto.LatLong
import com.example.driver.data.model.route.Direction
import com.example.driver.domain.usecase.AcceptBookingUseCase
import com.example.driver.domain.usecase.GetRouteNavigationUseCase
import com.example.driver.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StimulateViewModel @Inject constructor(
    private val acceptBookingUseCase: AcceptBookingUseCase,
    private val getRouteNavigationUseCase: GetRouteNavigationUseCase
): ViewModel() {
    val origin: LatLong? = null
    val destination: LatLong? = null

    fun acceptBooking() = liveData {
        emit(Response.loading(null))
        emit(acceptBookingUseCase.invoke())
    }

    fun doneDriving() = liveData {
        emit(Response.loading(null))
        runBlocking(Dispatchers.IO) {
        }
    }

    fun getNavigation() = liveData {
        emit(Response.loading(null))
        emit(getRouteNavigationUseCase.invoke(origin.toString(),destination.toString()))
    }
}