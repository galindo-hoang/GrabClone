package com.example.driver.presentation

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application(),LifecycleObserver {
    companion object {
        var token = ""
    }
}