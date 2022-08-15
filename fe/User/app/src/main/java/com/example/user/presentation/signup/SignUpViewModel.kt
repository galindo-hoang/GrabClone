package com.example.user.presentation.signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.user.data.dto.UserDto
import com.example.user.data.dto.ValidateOTP
import com.example.user.domain.usecase.SignUpPhoneNumberUseCase
import com.example.user.domain.usecase.SignUpSaveAccountUseCase
import com.example.user.domain.usecase.ValidateOtpUseCase
import com.example.user.utils.Response
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpViewModel @Inject constructor(
    private val signUpPhoneNumberUseCase: SignUpPhoneNumberUseCase,
    private val signUpSaveAccountUseCase: SignUpSaveAccountUseCase,
    private val validateOtpUseCase: ValidateOtpUseCase
): ViewModel() {

    private var _userName = MutableLiveData<String?>()
    private var _password = MutableLiveData<String?>()
    private var _rePassword = MutableLiveData<String?>()
    private var _isValidPassword = MutableLiveData(true)
    private var _phoneNumber = MutableLiveData<String?>()
    private var _isValidPhoneNumber = MutableLiveData(true)
    private var _otp = MutableLiveData<Int?>()
    private var _inputOtp = MutableLiveData<String>()
    private var _checkOtp = MutableLiveData<Boolean>()


    val userName get() = _userName
    val password get() = _password
    val confirmPassword get() = _rePassword
    val isValidPassword get() = _isValidPassword
    val phoneNumber get() = _phoneNumber
    val isValidPhoneNumber get() = _isValidPhoneNumber
    val otp get() = _otp
    val inputOtp get() = _inputOtp
    val checkOtp get() = _checkOtp


    fun signUpPhoneNumber() {
        if(_phoneNumber.value.toString().isNotEmpty()){
            var otp: Int
            runBlocking(Dispatchers.IO) {
                otp = signUpPhoneNumberUseCase.invoke(
                    UserDto(
                        password = _password.value,
                        username = _userName.value,
                        phoneNumber = "+84" + _phoneNumber.value?.substring(1))
                )
            }
            _otp.postValue(otp)
        }
    }

    val signUpSaveAccount = liveData(Dispatchers.IO) {
        if(confirmPassword.value == password.value &&
            (confirmPassword.value ?: "").isNotEmpty() &&
            (userName.value ?: "").isNotEmpty()
        ){
            emit(Response.loading("Loading data"))
            emit(
                signUpSaveAccountUseCase.invoke(
                    UserDto(
                        password = _password.value,
                        username = _userName.value,
                        phoneNumber = "+84" + _phoneNumber.value?.substring(1),
                        otp = _inputOtp.value
                    )
                )
            )
        }
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