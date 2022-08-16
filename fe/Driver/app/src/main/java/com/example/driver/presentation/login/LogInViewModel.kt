package com.example.driver.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.driver.data.dto.Login
import com.example.driver.domain.usecase.LoginUseCase
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class LogInViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {
    private var _userName: MutableLiveData<String> = MutableLiveData()
    val userName get() = _userName
    private var _password: MutableLiveData<String> = MutableLiveData()
    val password get() = _password
    private var _isLogin: MutableLiveData<Int> = MutableLiveData()
    val isLogin get() = _isLogin

    fun login() {
        if(_userName.value != null || _password.value != null){
            runBlocking {
                val user = loginUseCase.invoke(Login(_userName.value!!,_password.value!!))
                if(user == null) _isLogin.postValue(-1)
                else _isLogin.postValue(1)
            }
        }else _isLogin.postValue(0)
    }
}