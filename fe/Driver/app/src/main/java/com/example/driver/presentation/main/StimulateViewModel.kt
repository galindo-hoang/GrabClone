package com.example.driver.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.driver.data.dto.LatLong
import com.example.driver.domain.usecase.AcceptBookingUseCase
import com.example.driver.exception.ExpiredRefreshTokenExceptionCustom
import com.example.driver.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StimulateViewModel @Inject constructor(
    private val acceptBookingUseCase: AcceptBookingUseCase
): ViewModel() {
    val source: LatLong? = null
    val destination: LatLong? = null
    private val _haveBooking = MutableLiveData(false)
    val haveBooking get() = _haveBooking

    fun acceptBooking() = liveData {
        emit(Response.loading(null))
        var response: Response<Int>
        runBlocking {
            response = acceptBookingUseCase.invoke()
        }
        if(response.data == -2){
            throw ExpiredRefreshTokenExceptionCustom(response.message.toString())
        }else emit(response)
    }

    fun doneDriving() = liveData {
        emit(Response.loading(null))
        runBlocking(Dispatchers.IO) {

        }
    }
}