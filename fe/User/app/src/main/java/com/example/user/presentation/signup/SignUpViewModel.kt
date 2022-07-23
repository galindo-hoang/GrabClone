package com.example.user.presentation.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.user.data.dto.UserDto
import com.example.user.data.dto.ValidateOTP
import com.example.user.domain.usecase.SignUpUseCase
import com.example.user.domain.usecase.ValidateOtpUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val validateOtpUseCase: ValidateOtpUseCase
): ViewModel() {
    var isValidPassword = true
    var isValidPhoneNumber = true

//    private var _name = MutableLiveData<String>()
    private var _userName = MutableLiveData<String>()
    private var _password = MutableLiveData<String>()
    private var _rePassword = MutableLiveData<String>()
    private var _phoneNumber = MutableLiveData<String>()
    private var _otp = MutableLiveData<Int>()
    private var _inputOtp = MutableLiveData<String>()
    private var _checkOtp = MutableLiveData<Boolean>()


//    val name get() = _name
    val userName get() = _userName
    val password get() = _password
    val confirmPassword get() = _rePassword
    val phoneNumber get() = _phoneNumber
    val otp get() = _otp
    val inputOtp get() = _inputOtp
    val checkOtp get() = _checkOtp


    fun signUp() {
        var otp: Int
        runBlocking(Dispatchers.IO){
            otp= signUpUseCase.invoke(
                UserDto(
                    password = _password.value,
                    username = _userName.value,
                    phoneNumber = "+84" + _phoneNumber.value?.substring(1))
            )
        }
        _otp.postValue(otp)
    }

    fun checkOtp() = _checkOtp.postValue(_otp.value.toString() == _inputOtp.value)

    fun validateOTP(): Int {
        var responseCode: Int
        runBlocking(Dispatchers.IO) {
            responseCode = validateOtpUseCase.invoke(
                ValidateOTP(
                    onceTimePassword = _otp.value!!,
                    phoneNumber = "+84" + _phoneNumber.value?.substring(1)
                )
            )
        }
        if(responseCode == 1){
            _userName.postValue(null)
            _phoneNumber.postValue(null)
            _password.postValue(null)
            _rePassword.postValue(null)
            _otp.postValue(null)
        }
        return responseCode
    }
}