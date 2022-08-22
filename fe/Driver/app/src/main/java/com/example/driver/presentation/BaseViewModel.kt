package com.example.driver.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.driver.data.dto.Login
import com.example.driver.domain.usecase.LogOutUseCase
import com.example.driver.domain.usecase.LoginUseCase
import com.example.driver.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class BaseViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logOutUseCase: LogOutUseCase
): ViewModel() {
    var userName: String? = null
    var password: String? = null

    fun login() = liveData {
        emit(Response.loading(null))
        logOutUseCase.invoke()
        emit(loginUseCase.invoke(Login(userName!!,password!!)))
    }

    fun logout() {
        runBlocking(Dispatchers.IO) {
            logOutUseCase.invoke()
        }
    }
}