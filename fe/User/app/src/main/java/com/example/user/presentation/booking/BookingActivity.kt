package com.example.user.presentation.booking

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.example.user.R
import com.example.user.databinding.ActivityBookingBinding
import com.example.user.presentation.BaseActivity
import com.example.user.utils.Constant.REQUEST_CURRENT_LOCATION
import com.example.user.utils.Permissions
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookingActivity : BaseActivity() {
    @Inject
    lateinit var bookingViewModel: BookingViewModel

    private var currentLatLong: LatLng? = null
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityBookingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

    private fun getCurrentLocation(){
        if(Permissions.checkPermissions(this)){
            if(!Permissions.isEnableLocation(this)) Permissions.turnOnLocation(this)
            else {
                fusedLocationProviderClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
                    Looper.myLooper()
                )
            }
        }else {
            Permissions.requestPermissions(this)
        }
    }

    private val mLocationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            currentLatLong = LatLng(p0.lastLocation?.latitude ?: 0.0, p0.lastLocation?.longitude ?: 0.0)
            (supportFragmentManager
                .findFragmentById(R.id.map_view_in_booking_activity) as SupportMapFragment).let {
                it.getMapAsync { map ->
                    googleMap = map
                    googleMap.addMarker(MarkerOptions().position(currentLatLong!!).title("Marker"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLong!!))
                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CURRENT_LOCATION -> {
                if(!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",packageName,null)
                    intent.data = uri
                    startActivity(intent)
                }else{
                    Toast.makeText(this,"Permissions Granted", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}