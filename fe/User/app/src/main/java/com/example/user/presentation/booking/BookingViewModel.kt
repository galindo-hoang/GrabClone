package com.example.user.presentation.booking

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.user.data.dto.BookingDto
import com.example.user.data.dto.LatLong
import com.example.user.data.model.booking.ResponseBooking
import com.example.user.data.model.place.Address
import com.example.user.data.model.place.AddressFromText
import com.example.user.data.model.route.Direction
import com.example.user.domain.usecase.BookingCarUseCase
import com.example.user.domain.usecase.GetListAddressFromTextUseCase
import com.example.user.domain.usecase.GetRouteNavigationUseCase
import com.example.user.utils.Constant
import com.example.user.utils.PaymentMethod
import com.example.user.utils.Response
import com.example.user.utils.TypeCar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
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
    private val _bookingRider = MutableLiveData<Response<ResponseBooking>>()
    lateinit var paymentMethod: PaymentMethod
    lateinit var typeCar: TypeCar
    var isBooking = false

    val listAddress get() = _listAddress
    val routes get() = _routes
    val bookingRider get() = _bookingRider

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

    fun searchingDriver() {
        _bookingRider.postValue(Response.loading(null))
        var response: Response<ResponseBooking>
        runBlocking(Dispatchers.IO) {
            response = bookingCarUseCase.invoke(
                BookingDto(
                    destination = LatLong(destination!!.position.latitude,destination!!.position.longitude),
                    origin = LatLong(origin!!.position.latitude,origin!!.position.longitude),
                    paymentMethod = paymentMethod,
                    price = 100.0,
                    typeCar = typeCar
                )
            )
        }
        _bookingRider.postValue(response)
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