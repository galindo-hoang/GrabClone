package com.example.user.presentation.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.user.domain.usecase.LogOutUseCase
import com.example.user.domain.usecase.SplashUseCase
import com.example.user.utils.Permission
import com.example.user.utils.Response
import com.example.user.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val splashUseCase: SplashUseCase,
    private val logOutUseCase: LogOutUseCase
): ViewModel() {
    private val _checking = MutableLiveData<MutableList<Permission>>()

    fun checkLogin() = liveData(Dispatchers.IO){
        emit(Response.loading(null))
        val response = splashUseCase.invoke()
        if(response == 1) emit(Response.success(true))
        else {
            val logout = logOutUseCase.invoke()
            if(logout.data == 1) emit(Response.success(false))
            else emit(Response.error(null,"cant acces local database"))
        }
    }
}