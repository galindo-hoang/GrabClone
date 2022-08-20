package com.example.driver.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.driver.domain.usecase.StartListeningBookingUseCase
import com.example.driver.domain.usecase.StopListeningBookingUseCase
import com.example.driver.utils.Response
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val stopListeningBookingUseCase: StopListeningBookingUseCase,
    private val startListeningBookingUseCase: StartListeningBookingUseCase,
): ViewModel() {
    fun startListening() = liveData {
        emit(Response.loading(null))
        emit(startListeningBookingUseCase.invoke())
    }
    fun stopListening() = liveData {
        emit(Response.loading(null))
        emit(stopListeningBookingUseCase.invoke())
    }
}