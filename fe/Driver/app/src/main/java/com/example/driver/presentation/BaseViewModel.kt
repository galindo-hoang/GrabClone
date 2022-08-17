package com.example.driver.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.driver.data.dao.UserDao
import com.example.driver.data.dto.Login
import com.example.driver.domain.usecase.LogOutUseCase
import com.example.driver.domain.usecase.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class BaseViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val userDao: UserDao
): ViewModel() {
    var userName: String? = null
    var password: String? = null
    private var _isLogin: MutableLiveData<Int> = MutableLiveData()
    val isLogin get() = _isLogin

    fun login(){
        runBlocking(Dispatchers.IO){
            logOutUseCase.invoke()
            val user = loginUseCase.invoke(Login(userName!!,password!!))
            if(user == null) _isLogin.postValue(-1)
            else _isLogin.postValue(1)
        }
    }

    fun logout() {
        runBlocking(Dispatchers.IO) {
            logOutUseCase.invoke()
        }
    }
}