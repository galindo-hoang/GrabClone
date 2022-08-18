package com.example.driver.presentation.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.driver.domain.usecase.LogOutUseCase
import com.example.driver.domain.usecase.SplashUseCase
import com.example.driver.utils.Permission
import com.example.driver.utils.Response
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val splashUseCase: SplashUseCase,
    private val logOutUseCase: LogOutUseCase
): ViewModel() {
    private val _checking = MutableLiveData<MutableList<Permission>>()

    fun checkLogin() = liveData(Dispatchers.IO){
        emit(Response.loading(null))
        val response = splashUseCase.invoke()
        if(response.data == 1) emit(Response.success(true))
        else {
            val logout = logOutUseCase.invoke()
            if(logout.data == 1) emit(Response.success(false))
            else emit(Response.error(null,400,"cant acces local database"))
        }
    }
}