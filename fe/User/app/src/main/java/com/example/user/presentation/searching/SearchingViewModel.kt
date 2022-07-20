package com.example.user.presentation.searching

import androidx.lifecycle.MutableLiveData
import com.example.user.data.model.googlemap.ResultPlaceClient
import com.example.user.data.model.googlemap.Route
import com.example.user.domain.usecase.GetAddressFromPlaceId
import com.example.user.domain.usecase.GetRouteNavigationUseCase
import kotlinx.coroutines.*
import javax.inject.Inject

class SearchingViewModel @Inject constructor(
    private val getAddressFromPlaceId: GetAddressFromPlaceId,
    private val getRouteNavigationUseCase: GetRouteNavigationUseCase
) {
    private val _origin = MutableLiveData<ResultPlaceClient>()
    private val _destination = MutableLiveData<ResultPlaceClient>()
    private val _routes = MutableLiveData<List<Route>>()
    private val _resultPlaceClient = MutableLiveData<ResultPlaceClient>()


    val origin get() = _origin
    val destination get() = _destination
    val routes get() = _routes
    val resultPlaceClient get() = _resultPlaceClient

    fun searchingCar() {
        if(_origin.value != null && _destination.value != null){
            CoroutineScope(Dispatchers.IO).launch {
                val response = withContext(Dispatchers.Default) {
                    try {
                        getRouteNavigationUseCase.invoke(
                            _origin.value?.geometry?.location.toString() ?: "",
                            _destination.value?.geometry?.location.toString() ?: "",
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
        CoroutineScope(Dispatchers.IO).launch {
            val response = withContext(Dispatchers.Default) {
                try {
                    getAddressFromPlaceId.invoke(placeId)
                }catch (e:Exception){
                    e.printStackTrace()
                    null
                }
            }
            _resultPlaceClient.postValue(response?.data)
        }
    }
}