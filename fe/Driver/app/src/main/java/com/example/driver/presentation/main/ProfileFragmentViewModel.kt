package com.example.driver.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.driver.data.dto.UserDto
import com.example.driver.domain.usecase.GetUserUseCase
import com.example.driver.domain.usecase.LogOutUseCase
import com.example.driver.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileFragmentViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val getUserUseCase: GetUserUseCase
): ViewModel() {


    fun getUser() = liveData {
        emit(Response.loading(null))
        var response: Response<UserDto>
        withContext(Dispatchers.IO) { response = getUserUseCase.invoke() }
        emit(response)
    }

    fun logout() = liveData {
        emit(Response.loading(null))
        var response: Response<Int>
        withContext(Dispatchers.IO) { response = logOutUseCase.invoke() }
        emit(response)
    }
}