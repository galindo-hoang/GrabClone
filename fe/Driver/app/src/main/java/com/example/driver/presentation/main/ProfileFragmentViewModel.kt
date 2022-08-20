package com.example.driver.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.driver.domain.usecase.LogOutUseCase
import com.example.driver.utils.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileFragmentViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase
): ViewModel() {
    private val _logout = MutableLiveData<Response<Int>>()
    val logout get() = _logout

    fun logout(){
        _logout.postValue(Response.loading(null))
        CoroutineScope(Dispatchers.IO).launch {
            _logout.postValue(logOutUseCase.invoke())
        }
    }
}