package com.example.user.presentation.booking

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.example.user.data.dto.BookingDto
import com.example.user.data.dto.LatLong
import com.example.user.data.model.booking.ResponseBooking
import com.example.user.data.model.place.Address
import com.example.user.data.model.place.AddressFromText
import com.example.user.data.model.route.Route
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
    private val _textOrigin = MutableLiveData<String>()
    private val _textDestination = MutableLiveData<String>()
    private val _listAddress = MutableLiveData<Response<AddressFromText>>()
    var origin: Address? = null
    var destination: Address? = null
    private val _routes = MutableLiveData<Response<List<Route>>>()
    private val _labelButton = MutableLiveData(Constant.SEARCHING_ROUTE)
    private val _continuation = MutableLiveData(false)
    private val _bookingRider = MutableLiveData<Response<ResponseBooking>>()
    lateinit var paymentMethod: PaymentMethod
    lateinit var typeCar: TypeCar
    var isBooking = false

    val textOrigin get() = _textOrigin
    val textDestination get() = _textDestination
    val listAddress get() = _listAddress
    val routes get() = _routes
    val labelButton get() = _labelButton
    val continuation get() = _continuation
    val bookingRider get() = _bookingRider

    fun searchingRoute() {
        if(_labelButton.value == Constant.SEARCHING_ROUTE){
            if(origin != null && destination != null){
                _routes.postValue(Response.loading(null))
                var response: Response<List<Route>>
                runBlocking(Dispatchers.IO) {
                    response = getRouteNavigationUseCase.invoke(
                        TypeCar.CAR,
                        origin!!.position.toString(),
                        destination!!.position.toString(),
                    )
                }
                _routes.postValue(response)
            }
        }else {
            _continuation.postValue(true)
        }
    }

//    fun getAddress(placeId: String) {
//        _resultPlaceClient.postValue(Response.loading(null))
//        runBlocking(Dispatchers.IO) {
//            _resultPlaceClient.postValue(getAddressFromPlaceId.invoke(placeId))
//        }
//    }

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

    private var typing: Boolean = false
    fun getListAddress(text: String) {
        if(typing) {
            _listAddress.postValue(Response.loading(null))
            var response: Response<AddressFromText>
            runBlocking(Dispatchers.IO) {
                response = getAddressFromTextUseCase.invoke(text)
            }
            _listAddress.postValue(response)
        }else {
            typing = true
            Executors.newSingleThreadScheduledExecutor().schedule({
                typing = false
            },1,TimeUnit.SECONDS)
        }
    }
}