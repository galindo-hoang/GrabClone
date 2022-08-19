package com.example.user.presentation.booking

import androidx.lifecycle.MutableLiveData
import com.example.user.data.model.googlemap.ResultPlaceClient
import com.example.user.data.model.googlemap.Route
import com.example.user.domain.usecase.GetAddressFromPlaceId
import com.example.user.domain.usecase.GetRouteNavigationUseCase
import com.example.user.utils.Constant
import com.example.user.utils.Response
import kotlinx.coroutines.*
import javax.inject.Inject

class SearchingRouteViewModel @Inject constructor(
    private val getAddressFromPlaceId: GetAddressFromPlaceId,
    private val getRouteNavigationUseCase: GetRouteNavigationUseCase
) {
    private var _origin: ResultPlaceClient? = null
    private var _destination: ResultPlaceClient? = null
    private val _routes = MutableLiveData<Response<List<Route>?>>()
    private val _resultPlaceClient = MutableLiveData<Response<ResultPlaceClient?>>()
    private val _labelButton = MutableLiveData(Constant.SEARCHING_ROUTE)
    private val _continuation = MutableLiveData(false)


    val origin get() = _origin
    val destination get() = _destination
    val routes get() = _routes
    val resultPlaceClient get() = _resultPlaceClient
    val labelButton get() = _labelButton
    val continuation get() = _continuation

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