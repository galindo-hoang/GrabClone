package com.example.user.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user.data.dto.Login
import com.example.user.data.dto.UserDto
import com.example.user.domain.usecase.LoginUseCase
import com.example.user.presentation.BaseApplication.Companion.token
import com.example.user.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogInViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {
    private var _userName: MutableLiveData<String> = MutableLiveData()
    val userName get() = _userName
    private var _password: MutableLiveData<String> = MutableLiveData()
    val password get() = _password
    private var _isLogin: MutableLiveData<Response<UserDto>> = MutableLiveData()
    val isLogin get() = _isLogin

    fun login() = viewModelScope.launch {
        _isLogin.value = Response.loading(null)
        if(_userName.value != null || _password.value != null){
            var response: Response<UserDto>
            withContext(Dispatchers.IO) {
                response = loginUseCase.invoke(token, Login(_userName.value!!,_password.value!!))
            }
            _isLogin.value = response
        }else _isLogin.value = Response.error(null,-1,"Please write username and password")
    }
}