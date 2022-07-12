package com.example.user.presentation.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SignUpViewModel @Inject constructor(): ViewModel() {
    private var _name = MutableLiveData<String>()
    val name get() = _name
    private var _userName = MutableLiveData<String>()
    val userName get() = _userName
    private var _password = MutableLiveData<String>()
    val password get() = _password
    private var _rePassword = MutableLiveData<String>()
    val rePassword get() = _rePassword
    private var _phoneNumber = MutableLiveData<String>()
    val phoneNumber get() = _phoneNumber

    fun signUp(){}
}