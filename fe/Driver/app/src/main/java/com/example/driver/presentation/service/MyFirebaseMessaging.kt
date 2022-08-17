package com.example.driver.presentation.service

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("LogNotTimber")
class MyFirebaseMessaging: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.e("Service TAG", "Refreshed token: $token")
        super.onNewToken(token)

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("----", "From: ${remoteMessage.from}")
        if (remoteMessage.data.isNotEmpty()) {
            Log.e("----", "Message data payload: ${remoteMessage.data}")

        }
        remoteMessage.notification?.let {
            Log.e("------", "Message Notification Body: ${it.body}")
        }
    }

}