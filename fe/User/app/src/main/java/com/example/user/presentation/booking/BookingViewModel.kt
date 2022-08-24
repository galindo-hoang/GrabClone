package com.example.user.presentation.booking

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.user.data.dto.BookingDto
import com.example.user.data.dto.LatLong
import com.example.user.data.dto.Payment
import com.example.user.data.dto.Vehicle
import com.example.user.data.model.booking.ResponseBooking
import com.example.user.data.model.place.Address
import com.example.user.data.model.place.AddressFromText
import com.example.user.data.model.route.Direction
import com.example.user.domain.usecase.BookingCarUseCase
import com.example.user.domain.usecase.GetListAddressFromTextUseCase
import com.example.user.domain.usecase.GetRouteNavigationUseCase
import com.example.user.utils.Response
import com.example.user.utils.TypeCar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingViewModel @Inject constructor(
    private val getRouteNavigationUseCase: GetRouteNavigationUseCase,
    private val bookingCarUseCase: BookingCarUseCase,
    private val getAddressFromTextUseCase: GetListAddressFromTextUseCase
): ViewModel() {
    private val _listAddress = MutableLiveData<Response<AddressFromText>>()
    var origin: Address? = null
    var destination: Address? = null
    private val _routes = MutableLiveData<Response<Direction>>()
    var payment: Payment? = null
    var vehicle: Vehicle? = null
    var isBooking = false
    var distance: Int? = null

    val listAddress get() = _listAddress
    val routes get() = _routes



    fun clear() {
        origin = null
        destination = null
        payment = null
        vehicle = null
        isBooking = false
        distance = null
    }


    fun searchingRoute() = viewModelScope.launch {
        if(origin != null && destination != null){
            _routes.value = Response.loading(null)
            var response: Response<Direction>
            withContext(Dispatchers.IO) {
                response = getRouteNavigationUseCase.invoke(
                    TypeCar.CAR,
                    origin!!.position.toString(),
                    destination!!.position.toString(),
                )
            }
            _routes.value = response
        }else {
            _routes.value = Response.error(null,500,"Please write location")
        }
    }

    fun searchingDriver() = liveData {
        emit(Response.loading(null))
        var response: Response<ResponseBooking>
        withContext(Dispatchers.IO) {
            response = bookingCarUseCase.invoke(
                BookingDto(
                    destination = LatLong(destination!!.position.latitude,destination!!.position.longitude),
                    origin = LatLong(origin!!.position.latitude,origin!!.position.longitude),
                    paymentMethod = payment!!.method,
                    price = vehicle!!.getPrice().toDouble(),
                    typeCar = vehicle!!.typeCar
                )
            )
        }
        emit(response)
    }

    fun getListAddress(text: String) = viewModelScope.launch {
        _listAddress.value = Response.loading(null)
        var response: Response<AddressFromText>
        withContext(Dispatchers.IO) {
            response = getAddressFromTextUseCase.invoke(text)
        }
        _listAddress.value = response
    }
}