package com.example.user.presentation.booking

import androidx.lifecycle.MutableLiveData
import com.example.user.data.model.googlemap.ResultPlaceClient
import com.example.user.data.model.googlemap.Route
import com.example.user.domain.usecase.GetAddressFromPlaceId
import com.example.user.domain.usecase.GetRouteNavigationUseCase
import com.example.user.utils.Response
import kotlinx.coroutines.*
import javax.inject.Inject

class BookingViewModel @Inject constructor(
    private val getAddressFromPlaceId: GetAddressFromPlaceId,
    private val getRouteNavigationUseCase: GetRouteNavigationUseCase
) {
    private var _origin: ResultPlaceClient? = null
    private var _destination: ResultPlaceClient? = null
    private val _routes = MutableLiveData<List<Route>?>()
    private val _resultPlaceClient = MutableLiveData<Response<ResultPlaceClient?>>()


    val origin get() = _origin
    val destination get() = _destination
    val routes get() = _routes
    val resultPlaceClient get() = _resultPlaceClient

    fun setDestination(value: ResultPlaceClient?) {this._destination = value}
    fun setOrigin(value: ResultPlaceClient?) {this._origin = value}

    fun searchingCar() {
        if(_origin != null && _destination != null){
            CoroutineScope(Dispatchers.IO).launch {
                val response = withContext(Dispatchers.Default) {
                    try {
                        getRouteNavigationUseCase.invoke(
                            _origin!!.geometry.location.toString(),
                            _destination!!.geometry.location.toString(),
                            "driving"
                        )
                    }catch (e:Exception){
                        e.printStackTrace()
                        null
                    }
                }
                if (response?.data != null && response.data.isNotEmpty()) {
                    _routes.postValue(response.data)
                }else{
                    _routes.postValue(listOf())
                }
            }
        }
    }

    fun getAddress(placeId: String) {
        _resultPlaceClient.postValue(Response.loading(null))
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _resultPlaceClient.postValue(getAddressFromPlaceId.invoke(placeId))
            }catch (e:Exception){
                e.printStackTrace()
                _resultPlaceClient.postValue(Response.error(null,e.message.toString()))
            }
        }
    }
}