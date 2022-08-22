package com.example.user.presentation.booking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.user.data.dto.BookingDto
import com.example.user.data.dto.LatLong
import com.example.user.data.dto.Payment
import com.example.user.data.dto.Vehicle
import com.example.user.data.model.place.Address
import com.example.user.data.model.place.AddressFromText
import com.example.user.data.model.route.Direction
import com.example.user.domain.usecase.BookingCarUseCase
import com.example.user.domain.usecase.GetListAddressFromTextUseCase
import com.example.user.domain.usecase.GetRouteNavigationUseCase
import com.example.user.utils.Response
import com.example.user.utils.TypeCar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingViewModel @Inject constructor(
    private val getRouteNavigationUseCase: GetRouteNavigationUseCase,
    private val bookingCarUseCase: BookingCarUseCase,
    private val getAddressFromTextUseCase: GetListAddressFromTextUseCase
) {
    private val _listAddress = MutableLiveData<Response<AddressFromText>>()
    var origin: Address? = Address(LatLong(10.838685,106.665255),"")
    var destination: Address? = Address(LatLong(10.799194,106.680264),"")
    private val _routes = MutableLiveData<Response<Direction>>()
    lateinit var payment: Payment
    lateinit var car: Vehicle
    var isBooking = false
    var distance: Int? = null

    val listAddress get() = _listAddress
    val routes get() = _routes

    fun searchingRoute() {
        if(origin != null && destination != null){
            _routes.postValue(Response.loading(null))
            var response: Response<Direction>
            runBlocking(Dispatchers.IO) {
                response = getRouteNavigationUseCase.invoke(
                    TypeCar.CAR,
                    origin!!.position.toString(),
                    destination!!.position.toString(),
                )
            }
            _routes.postValue(response)
        }else {
            _routes.postValue(
                Response.error(null,500,"Please write location")
            )
        }
    }

    fun searchingDriver() = liveData {
        emit(Response.loading(null))
        emit(
            bookingCarUseCase.invoke(
                BookingDto(
                    destination = LatLong(destination!!.position.latitude,destination!!.position.longitude),
                    origin = LatLong(origin!!.position.latitude,origin!!.position.longitude),
                    paymentMethod = payment.method,
                    price = car.getPrice().toDouble(),
                    typeCar = car.typeCar
                )
            )
        )
    }

    fun getListAddress(text: String) {
        _listAddress.postValue(Response.loading(null))
        var response: Response<AddressFromText>
        runBlocking(Dispatchers.IO) {
            response = getAddressFromTextUseCase.invoke(text)
        }
        _listAddress.postValue(response)
    }
}