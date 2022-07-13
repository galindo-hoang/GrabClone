package com.example.user.presentation.searching

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.user.data.model.ResultPlaceClient
import com.example.user.data.model.Route
import com.example.user.domain.usecase.GetAddressFromPlaceId
import com.example.user.domain.usecase.GetRouteNavigationUseCase
import com.example.user.utils.Response
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
            if (response != null) {
                _routes.postValue(response.data)
            }
        }
    }

    fun getAddress(placeId: String) {
//        emit(Response.loading(null))
//        try {
//            emit(getAddressFromPlaceId.invoke(placeId))
//        }catch (e: Exception){
//            e.printStackTrace()
//        }
        CoroutineScope(Dispatchers.IO).launch {
            Log.e("+++++++++++",placeId)
            val response = withContext(Dispatchers.Default) {
                try {
                    val b = getAddressFromPlaceId.invoke(placeId)
                    Log.e("=========", b.toString())
                    b
                }catch (e:Exception){
                    e.printStackTrace()
                    null
                }
            }
            Log.e("----------", response?.data.toString())
            _resultPlaceClient.postValue(response?.data)
        }
    }
}