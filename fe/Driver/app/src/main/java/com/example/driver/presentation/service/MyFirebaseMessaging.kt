package com.example.driver.presentation.service

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import com.example.driver.data.dto.RegisterFCMBody
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.domain.repository.BookingRepository
import com.example.driver.utils.Constant
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@SuppressLint("LogNotTimber")
class MyFirebaseMessaging: FirebaseMessagingService() {
    @Inject
    lateinit var authenticationRepository: AuthenticationRepository
    @Inject
    lateinit var bookingRepository: BookingRepository
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
        if (remoteMessage.data.isNotEmpty() && remoteMessage.data.containsKey("booking")) {
            sendBroadcast(
                Intent(Constant.HAVE_NEW_BOOKING).apply {
                    this.putExtra(
                        Constant.HAVE_NEW_BOOKING_EXTRA,
                        remoteMessage.data["booking"]
                    )
                }
            )
        }
    }

}