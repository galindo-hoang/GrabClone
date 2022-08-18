package com.example.user.presentation.booking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.user.data.dto.BookingDto
import com.example.user.data.dto.LatLong
import com.example.user.domain.usecase.BookingCarUseCase
import com.example.user.exception.ExpiredRefreshTokenExceptionCustom
import com.example.user.utils.Response
import com.example.user.utils.VehicleType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class BookingViewModel @Inject constructor(
    private val bookingCarUseCase: BookingCarUseCase
): ViewModel() {
    private val _bookingRider = MutableLiveData<Response<Int>>()
    val bookingRider get() = _bookingRider
    fun check(){
        try {
            throw ExpiredRefreshTokenExceptionCustom("response.message.toString()")
        } catch (e:Exception){
            throw e
        }
    }
    fun searchingDriver(){
        _bookingRider.postValue(Response.loading(0))
        var response: Response<Int>
        runBlocking(Dispatchers.IO) {
            response = bookingCarUseCase.invoke(BookingDto(
                destination = LatLong(0.0,0.0),
                source = LatLong(0.0,0.0),
                phoneNumber = "",
                vehicleType = VehicleType.SEDAN
            ))
        }
        if(response.data == -2){
            throw ExpiredRefreshTokenExceptionCustom(response.message.toString())
        }else _bookingRider.postValue(response)
    }
}