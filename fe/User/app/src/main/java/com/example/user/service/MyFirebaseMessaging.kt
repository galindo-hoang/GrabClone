package com.example.user.service

import android.content.Intent
import android.os.Handler
import android.util.Log
import com.example.user.data.dto.CurrentLocationDriver
import com.example.user.data.dto.RegisterFCMBody
import com.example.user.domain.repository.AuthenticationRepository
import com.example.user.domain.repository.BookingRepository
import com.example.user.presentation.BaseApplication
import com.example.user.utils.Constant
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MyFirebaseMessaging: FirebaseMessagingService() {
    @Inject
    lateinit var authenticationRepository: AuthenticationRepository
    @Inject
    lateinit var bookingRepository: BookingRepository

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        BaseApplication.token = token
        Log.e("Service TAG", "Refreshed token: $token")
        runBlocking(Dispatchers.IO) {
            try {
                val userDto = authenticationRepository.getAccount()
                bookingRepository.postRegisterFcmToken(RegisterFCMBody(token, userDto.username!!))
            }catch (e:Exception) {Log.e("-------", e.message.toString())}
        }
    }
    companion object {
        var countWaiting = 0
        var isWaiting = false
        var isMoving = false
        lateinit var mainHandler: Handler
        private val waitingDriver = object : Runnable {
            override fun run() {
                if(countWaiting < 20){
                    countWaiting += 1
                    mainHandler.postDelayed(this, 1000)
                }else {
                    stopListening()
                }
            }
        }

        fun startListening(){
            countWaiting = 0
            isWaiting = true
            mainHandler.post(waitingDriver)
        }

        fun stopListening(){
            isWaiting = false
            mainHandler.removeCallbacks(waitingDriver)
        }

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("-------",remoteMessage.data.toString())
        if(remoteMessage.data.isNotEmpty() && remoteMessage.data.containsKey("booking")) {
            if(isWaiting){
                sendBroadcast(
                    Intent(Constant.HAVE_DRIVER).apply {
                        this.putExtra(
                            Constant.HAVE_DRIVER_STRING,
                            remoteMessage.data["booking"]
                        )
                    }
                )
                isWaiting = false
                isMoving = true
            }
        }
        if(remoteMessage.data.isNotEmpty() && remoteMessage.data.containsKey("ride")) {
            val stringRide = remoteMessage.data["ride"]?.replace("\\","")?.replace("\"{","{")?.replace("}\"","}")
            Log.e("=======",stringRide.toString())
            sendBroadcast(
                Intent(Constant.UPDATE_LOCATION_DRIVER).apply {
                    this.putExtra(Constant.UPDATE_LOCATION_DRIVER_STRING,stringRide)
                }
            )
        }
        if(remoteMessage.data.isNotEmpty() && remoteMessage.data.containsKey("finish_ride")) {
            sendBroadcast(
                Intent(Constant.FINISH_MOVING).apply {
                    this.putExtra(
                        Constant.FINISH_MOVING_STRING,
                        remoteMessage.data["ride"]
                    )
                }
            )
            isMoving = false
        }
    }
}