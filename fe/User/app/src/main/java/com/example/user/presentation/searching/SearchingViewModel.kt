package com.example.user.presentation.searching

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.user.domain.usecase.GetRouteNavigationUseCase
import com.example.user.utils.Response
import javax.inject.Inject

class SearchingViewModel @Inject constructor(
    private val getRouteNavigationUseCase: GetRouteNavigationUseCase
) {
    private val _origin = MutableLiveData("Your current location")
    val origin get() = _origin
    private val _destination = MutableLiveData<String>()
    val destination get() = _destination
    private val _title = MutableLiveData<String>()
    val title get() = _title

    fun getRoutes() = liveData{
        emit(Response.loading(null))
        try {
            emit(getRouteNavigationUseCase.invoke(_origin.value ?: "", _destination.value ?: "","driving"))
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}