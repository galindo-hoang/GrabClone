package com.example.user.presentation

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessaging: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.e("Service TAG", "Refreshed token: $token")
        super.onNewToken(token)

    }
}