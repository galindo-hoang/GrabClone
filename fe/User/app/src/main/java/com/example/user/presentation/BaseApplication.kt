package com.example.user.presentation

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidApp
class BaseApplication: Application(),LifecycleObserver {
    private lateinit var inten: Intent
    override fun onCreate() {
        super.onCreate()
        inten = Intent(applicationContext, BackgroundService::class.java)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        stopService(inten)
        Log.e("MyApp", "App in background")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        startService(inten)
        Log.e("MyApp", "App in foreground")
    }


    override fun onTerminate() {
        super.onTerminate()
        Log.e("--","terminate")
    }
}