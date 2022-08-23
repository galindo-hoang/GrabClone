package com.example.driver.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.driver.domain.usecase.LogOutUseCase
import com.example.driver.utils.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileFragmentViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase
): ViewModel() {

    fun logout() = liveData {
        emit(Response.loading(null))
        var response: Response<Int>
        withContext(Dispatchers.IO) { response = logOutUseCase.invoke() }
        emit(response)
    }
}