package com.example.driver.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.driver.data.model.fcm.ResponseSubscribe
import com.example.driver.data.model.fcm.ResponseUnSubscribe
import com.example.driver.domain.usecase.StartListeningBookingUseCase
import com.example.driver.domain.usecase.StopListeningBookingUseCase
import com.example.driver.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val stopListeningBookingUseCase: StopListeningBookingUseCase,
    private val startListeningBookingUseCase: StartListeningBookingUseCase,
): ViewModel() {
    fun startListening() = liveData {
        emit(Response.loading(null))
        var response: Response<ResponseSubscribe>
        withContext(Dispatchers.IO) { response = startListeningBookingUseCase.invoke() }
        emit(response)
    }
    fun stopListening() = liveData {
        emit(Response.loading(null))
        var response: Response<ResponseUnSubscribe>
        withContext(Dispatchers.IO) {
            response = stopListeningBookingUseCase.invoke()
        }
        emit(response)
    }
}