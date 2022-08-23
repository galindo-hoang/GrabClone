package com.example.user.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.user.domain.usecase.LogOutUseCase
import com.example.user.domain.usecase.SplashUseCase
import com.example.user.utils.Response
import com.example.user.utils.Status
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val splashUseCase: SplashUseCase,
    private val logOutUseCase: LogOutUseCase
): ViewModel() {

    fun checkLogin() = liveData {
        emit(Response.loading(null))
        val response = splashUseCase.invoke()
        if(response.status == Status.SUCCESS && response.data == 1) {
            emit(Response.success(true))
        }
        else {
            val logout = logOutUseCase.invoke()
            if(logout.status == Status.SUCCESS) emit(Response.success(false))
            else emit(logout)
        }
    }
}