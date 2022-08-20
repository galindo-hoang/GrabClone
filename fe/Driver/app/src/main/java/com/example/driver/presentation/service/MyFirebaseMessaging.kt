package com.example.driver.presentation.service

import android.annotation.SuppressLint
import android.util.Log
import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.data.model.ReceiveBookingFCM
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.domain.repository.BookingRepository
import com.example.driver.presentation.main.StimulateViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@SuppressLint("LogNotTimber")
class MyFirebaseMessaging: FirebaseMessagingService() {
    @Inject
    lateinit var authenticationRepository: AuthenticationRepository
    @Inject
    lateinit var bookingRepository: BookingRepository
    @Inject
    lateinit var stimulateViewModel: StimulateViewModel
    override fun onNewToken(token: String) {
        Log.e("Service TAG", "Refreshed token: $token")
        super.onNewToken(token)

        runBlocking(Dispatchers.IO) {
            try {
                val userDto = authenticationRepository.getAccount()
                bookingRepository.postRegisterFcmToken(RegisterFCMBody(token, userDto.username!!))
            }catch (e:Exception) {Log.e("-------", e.message.toString())}
        }


    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("-----------",this.toString())
        if (remoteMessage.data.isNotEmpty() && remoteMessage.data.containsKey("booking")) {
            val a = Gson().fromJson(remoteMessage.data["booking"],ReceiveBookingFCM::class.java)
            stimulateViewModel.haveBooking.postValue(true)
        }
        remoteMessage.notification?.let {
            Log.e("------", "Message Notification Body: ${it.body}")
        }
    }

}