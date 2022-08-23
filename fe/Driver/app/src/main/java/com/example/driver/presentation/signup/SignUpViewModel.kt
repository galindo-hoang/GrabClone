package com.example.driver.presentation.signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.driver.data.dto.UserDto
import com.example.driver.data.dto.ValidateOTP
import com.example.driver.data.model.authentication.BodyValidateOrRegister
import com.example.driver.domain.usecase.SignUpPhoneNumberUseCase
import com.example.driver.domain.usecase.SignUpSaveAccountUseCase
import com.example.driver.domain.usecase.ValidateOtpUseCase
import com.example.driver.utils.Response
import com.example.driver.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
    private var _checkOtp = MutableLiveData<Response<BodyValidateOrRegister>>()


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
            var otp = -1
            runBlocking(Dispatchers.IO) {
                val response = signUpPhoneNumberUseCase.invoke(
                    UserDto(
                        password = _password.value,
                        username = _userName.value,
                        phoneNumber = "+84" + _phoneNumber.value?.substring(1))
                )
                if(response.status == Status.SUCCESS) otp = response.data!!
            }
            _otp.postValue(otp)
        }
    }

    val signUpSaveAccount = liveData {
        emit(Response.loading("Loading data"))
        Log.e("++++++++",_phoneNumber.value.toString())
        Log.e("++++++++",_userName.value.toString())
        if(_rePassword.value == _password.value && (_rePassword.value ?: "").isNotEmpty() && (_userName.value ?: "").isNotEmpty()) {
            var response: Response<String>
            withContext(Dispatchers.IO) {
                response = signUpSaveAccountUseCase.invoke(
                    UserDto(
                        password = _password.value,
                        username = _userName.value,
                        phoneNumber = "+84" + _phoneNumber.value?.substring(1),
                        otp = _inputOtp.value
                    )
                )
            }
            emit(response)
        } else emit(Response.error(null,-1,"please write data"))
    }

    fun validateOTP() = viewModelScope.launch {
        _checkOtp.value = Response.loading(null)
        var response: Response<BodyValidateOrRegister>
        withContext(Dispatchers.IO) {
            response = validateOtpUseCase.invoke(
                ValidateOTP(
                    onceTimePassword = _otp.value!!,
                    phoneNumber = "+84" + _phoneNumber.value?.substring(1)
                )
            )
        }
        _checkOtp.value = response
    }

    fun clear() {
        _userName.postValue(null)
        _phoneNumber.postValue(null)
        _password.postValue(null)
        _rePassword.postValue(null)
        _otp.postValue(null)
    }
}