package com.example.user.presentation.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.user.domain.usecase.LoginUseCase
import javax.inject.Inject

class LogInViewModel @Inject constructor(
//    private val loginUseCase: LoginUseCase
): ViewModel() {
    private var _userName: MutableLiveData<String> = MutableLiveData()
    val userName get() = _userName
    private var _password: MutableLiveData<String> = MutableLiveData()
    val password get() = _password

    fun login(){
        _userName.value?.let { Log.e("---", it) }
        _password.value?.let { Log.e("---", it) }
    }
}