package com.example.user.presentation

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.example.user.utils.Constant

class BackgroundService: Service() {
    private var count = 0
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("service","start")
        mainHandler.post(updateTextTask)
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        mainHandler = Handler(Looper.myLooper()!!)
    }

    override fun onDestroy() {
        Log.e("service","destroy")
        mainHandler.removeCallbacks(updateTextTask)
        super.onDestroy()
    }


    lateinit var mainHandler: Handler
    private val updateTextTask = object : Runnable {
        override fun run() {
            ++count
            val inten = Intent(Constant.SERVICE_ACCESS_TOKEN).apply {
                this.putExtra(Constant.SERVICE_ACCESS_TOKEN_BOOLEAN,count)
            }
            sendBroadcast(inten)
            mainHandler.postDelayed(this, 1000)
        }
    }
}