package com.example.driver.presentation.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.example.driver.data.dto.LatLong
import com.example.driver.domain.usecase.SendCurrentLocationBeforeAcceptUseCase
import com.example.driver.utils.Constant
import com.example.driver.utils.RequestPermissions
import com.example.driver.utils.Status
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
@AndroidEntryPoint
class CurrentLocationService: Service() {
    @Inject
    lateinit var sendCurrentLocationBeforeAcceptUseCase: SendCurrentLocationBeforeAcceptUseCase
    override fun onBind(p0: Intent?): IBinder? = null

    private var currentLatLong: LatLong? = null
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var loopUpdateLocationHandler: Handler
    private val loopUpdateLocation = object : Runnable {
        override fun run() {
            this@CurrentLocationService.getCurrentLocation()
            loopUpdateLocationHandler.postDelayed(this, 1000)
        }
    }
    override fun onCreate() {
        super.onCreate()
        loopUpdateLocationHandler = Handler(Looper.myLooper()!!)
        setUpCurrentRequestLocation()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        loopUpdateLocationHandler.post(loopUpdateLocation)
        return START_STICKY
    }
    override fun onDestroy() {
        loopUpdateLocationHandler.removeCallbacks(loopUpdateLocation)
        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
        Log.e("service","stop")
        super.onDestroy()
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun setUpCurrentRequestLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        mLocationRequest = LocationRequest.create()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { mLocationRequest.priority = android.location.LocationRequest.QUALITY_HIGH_ACCURACY }
        mLocationRequest.interval = 1000
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
//            currentLatLong = LatLong(p0.lastLocation?.latitude ?: 0.0, p0.lastLocation?.longitude ?: 0.0)
            currentLatLong = LatLong(10.7624371,106.6823388)
            Log.e("current location",currentLatLong.toString())
            runBlocking(Dispatchers.IO) {
                val response = sendCurrentLocationBeforeAcceptUseCase.invoke(currentLatLong!!)
                if(response.status == Status.ERROR && response.codeResponse == -2){
                    val intent = Intent(Constant.REFRESH_TOKEN_EXPIRED_WHEN_SEND_LOCATION).apply {
                        this.putExtra(Constant.REFRESH_TOKEN_EXPIRED_WHEN_SEND_LOCATION_EXTRA_USERNAME,response.message)
                    }
                    sendBroadcast(intent)
                }
            }
        }
    }
}