package com.example.user.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.user.data.dto.BookingDto
import com.example.user.data.dto.LatLong
import com.example.user.domain.usecase.BookingCarUseCase
import com.example.user.utils.VehicleType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class BookingViewModel @Inject constructor(
    private val bookingCarUseCase: BookingCarUseCase
): ViewModel() {
    fun searchingDriver(){
        runBlocking {
            val response = bookingCarUseCase.invoke(BookingDto(
                destination = LatLong(0.0,0.0),
                source = LatLong(0.0,0.0),
                phoneNumber = "",
                vehicleType = VehicleType.SEDAN
            ))
            Log.e("checking----------",response.body().toString())
        }
    }
}