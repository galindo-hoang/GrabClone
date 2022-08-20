package com.example.driver.domain.usecase

import android.app.Application
import android.content.Intent
import com.example.driver.presentation.service.CurrentLocationService
import javax.inject.Inject
import javax.inject.Singleton
// trash
@Singleton
class SetupServiceCurrentLocationUseCase @Inject constructor() {
    private lateinit var intent: Intent
    private var application: Application? = null
    fun start(application: Application) {
        if(this.application == null){
            this.application = application
            intent = Intent(application,CurrentLocationService::class.java)
            application.startService(intent)
        }
    }

    fun stop() {
        if(this.application != null) {
            this.application!!.stopService(intent)
            this.application = null
        }

    }
}