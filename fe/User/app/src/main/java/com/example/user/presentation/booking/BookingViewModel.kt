package com.example.user.presentation.booking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.user.data.dto.BookingDto
import com.example.user.data.dto.LatLong
import com.example.user.data.model.booking.ResponseBooking
import com.example.user.data.model.googlemap.ResultPlaceClient
import com.example.user.data.model.googlemap.Route
import com.example.user.data.model.place.AddressFromText
import com.example.user.domain.usecase.BookingCarUseCase
import com.example.user.domain.usecase.GetAddressFromPlaceId
import com.example.user.domain.usecase.GetListAddressFromTextUseCase
import com.example.user.domain.usecase.GetRouteNavigationUseCase
import com.example.user.utils.Constant
import com.example.user.utils.PaymentMethod
import com.example.user.utils.Response
import com.example.user.utils.TypeCar
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingViewModel @Inject constructor(
    private val getAddressFromPlaceId: GetAddressFromPlaceId,
    private val getRouteNavigationUseCase: GetRouteNavigationUseCase,
    private val bookingCarUseCase: BookingCarUseCase,
    private val getAddressFromTextUseCase: GetListAddressFromTextUseCase
) {
    private val _textOrigin = MutableLiveData<String>()
    private val _textDestination = MutableLiveData<String>()
    private val _listAddress = MutableLiveData<Response<AddressFromText>>()
    private var _origin: ResultPlaceClient? = null
    private var _destination: ResultPlaceClient? = null
    private val _routes = MutableLiveData<Response<List<Route>?>>()
    private val _resultPlaceClient = MutableLiveData<Response<ResultPlaceClient?>>()
    private val _labelButton = MutableLiveData(Constant.SEARCHING_ROUTE)
    private val _continuation = MutableLiveData(false)
    private val _bookingRider = MutableLiveData<Response<ResponseBooking>>()
    lateinit var paymentMethod: PaymentMethod
    lateinit var typeCar: TypeCar
    var isBooking = false

    val textOrigin get() = _textOrigin
    val textDestination get() = _textDestination
    val listAddress get() = _listAddress
    val origin get() = _origin
    val destination get() = _destination
    val routes get() = _routes
    val resultPlaceClient get() = _resultPlaceClient
    val labelButton get() = _labelButton
    val continuation get() = _continuation
    val bookingRider get() = _bookingRider

    fun setDestination(value: ResultPlaceClient?) {this._destination = value}
    fun setOrigin(value: ResultPlaceClient?) {this._origin = value}

    fun searchingRoute() {
        if(_labelButton.value == Constant.SEARCHING_ROUTE){
            if(_origin != null && _destination != null){
                _routes.postValue(Response.loading(null))
                var response: Response<List<Route>?>
                runBlocking(Dispatchers.IO) {
                    response = getRouteNavigationUseCase.invoke(
                        _origin!!.geometry.location.toString(),
                        _destination!!.geometry.location.toString(),
                        "driving"
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
                    destination = LatLong(_destination!!.geometry.location.lat,_destination!!.geometry.location.lng),
                    origin = LatLong(_origin!!.geometry.location.lat,_origin!!.geometry.location.lng),
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