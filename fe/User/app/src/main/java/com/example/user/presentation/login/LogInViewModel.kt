package com.example.user.presentation.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.user.data.dto.Login
import com.example.user.domain.usecase.LoginUseCase
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
                if(user == null) isLogin.postValue(-1)
                else isLogin.postValue(1)
            }
        }else _isLogin.postValue(0)
    }
}