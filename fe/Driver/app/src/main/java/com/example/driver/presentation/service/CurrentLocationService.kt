package com.example.driver.presentation.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.example.driver.data.dto.LatLong
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.domain.repository.BookingRepository
import com.example.driver.domain.usecase.SendCurrentLocationService
import com.example.driver.utils.Constant
import com.example.driver.utils.RequestPermissions
import com.example.driver.utils.Status
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CurrentLocationService: Service() {
    @Inject
    lateinit var bookingRepository: BookingRepository
    @Inject
    lateinit var authenticationRepository: AuthenticationRepository
    @Inject
    lateinit var sendCurrentLocationService: SendCurrentLocationService
    override fun onBind(p0: Intent?): IBinder? = null

    private var currentLatLong: LatLong? = null
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()
        mainHandler = Handler(Looper.myLooper()!!)
        setUpCurrentRequestLocation()
        getCurrentLocation()
    }

    private fun setUpCurrentRequestLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationRequest = LocationRequest.create()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mLocationRequest.priority = android.location.LocationRequest.QUALITY_HIGH_ACCURACY
        }
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(){
        if(RequestPermissions.checkPermissions(this)){
            if(!RequestPermissions.isEnableLocation(this)) RequestPermissions.turnOnLocation(this)
            else {
                fusedLocationProviderClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
                    Looper.myLooper()
                )
            }
        }
    }
    private val mLocationCallback = object : LocationCallback() {
        @SuppressLint("LogNotTimber")
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            currentLatLong = LatLong(p0.lastLocation?.latitude ?: 0.0, p0.lastLocation?.longitude ?: 0.0)
            runBlocking(Dispatchers.IO) {
                val response = sendCurrentLocationService.invoke(currentLatLong!!)
                if(response.status == Status.ERROR && response.codeResponse == -2){
                    val intent = Intent(Constant.REFRESH_TOKEN_EXPIRED).apply {
                        this.putExtra(Constant.REFRESH_TOKEN_EXPIRED_PUT_EXTRA_USERNAME,response.message)
                    }
                    sendBroadcast(intent)
                }
            }
        }
    }

    lateinit var mainHandler: Handler
    private val looper = object : Runnable {
        override fun run() {
            this@CurrentLocationService.getCurrentLocation()
            mainHandler.postDelayed(this, 1000)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mainHandler.post(looper)
        return START_STICKY
    }

    override fun onDestroy() {
        mainHandler.removeCallbacks(looper)
        super.onDestroy()
    }
}