package com.example.user.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user.data.dto.Login
import com.example.user.data.dto.UserDto
import com.example.user.data.model.authentication.BodyAccessToken
import com.example.user.domain.usecase.LogOutUseCase
import com.example.user.domain.usecase.LoginUseCase
import com.example.user.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BaseViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logOutUseCase: LogOutUseCase
): ViewModel() {
    var userName: String? = null
    var password: String? = null
    private var _isLogin: MutableLiveData<Response<UserDto>> = MutableLiveData()
    val isLogin get() = _isLogin

    fun login(token: String) = viewModelScope.launch {
        _isLogin.value = Response.loading(null)
        var response: Response<UserDto>
        withContext(Dispatchers.IO){
            logOutUseCase.invoke()
            response = loginUseCase.invoke(token, Login(userName!!,password!!))
        }
        _isLogin.value = response
    }

    fun logout() {
        //best case
        runBlocking(Dispatchers.IO) {
            logOutUseCase.invoke()
        }
    }
}