package com.example.user.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.user.domain.usecase.LogOutUseCase
import com.example.user.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase
): ViewModel() {

    fun logout() = liveData {
        emit(Response.loading(null))
        var response: Response<Int>
        withContext(Dispatchers.IO) {
            response = logOutUseCase.invoke()
        }
        emit(response)
    }
}