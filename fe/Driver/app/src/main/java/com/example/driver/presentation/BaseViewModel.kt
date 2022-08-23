package com.example.driver.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.driver.data.dto.Login
import com.example.driver.data.dto.UserDto
import com.example.driver.data.model.authentication.BodyAccessToken
import com.example.driver.domain.usecase.LogOutUseCase
import com.example.driver.domain.usecase.LoginUseCase
import com.example.driver.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BaseViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logOutUseCase: LogOutUseCase
): ViewModel() {
    var userName: String? = null
    var password: String? = null

    fun login(token: String) = liveData {
        emit(Response.loading(null))
        var response: Response<UserDto>
        withContext(Dispatchers.IO) {
            logOutUseCase.invoke()
            response = loginUseCase.invoke(token, Login(userName!!,password!!))
        }
        emit(response)
    }

    fun logout() {
        runBlocking(Dispatchers.IO) {
            logOutUseCase.invoke()
        }
    }
}