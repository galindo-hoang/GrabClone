package com.example.user.presentation.booking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.user.BuildConfig
import com.example.user.R
import com.example.user.data.dto.CurrentLocationDriver
import com.example.user.data.dto.LatLong
import com.example.user.databinding.ActivityRoutingBinding
import com.example.user.databinding.ActivitySearchingRouteBinding
import com.example.user.presentation.base.BaseActivity
import com.example.user.presentation.main.MainActivity
import com.example.user.utils.Constant
import com.example.user.utils.TypeCar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class RoutingActivity : BaseActivity() {
    private var marker: Marker? = null
    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityRoutingBinding
    @Inject
    lateinit var bookingViewModel: BookingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupGoogleMap()
        registerReceiver(isFinishMoving, IntentFilter(Constant.FINISH_MOVING))
        registerReceiver(updateDriverLocation,IntentFilter(Constant.UPDATE_LOCATION_DRIVER))
    }

    private fun setupGoogleMap() {
        if(!Places.isInitialized()) Places.initialize(this, BuildConfig.GOOGLE_MAP_API)
        (supportFragmentManager.findFragmentById(R.id.map_view_in_routing_activity) as SupportMapFragment).getMapAsync {
            this.map = it
            setupUI()
        }
    }

    private fun setupUI() {
        val feature = bookingViewModel.routesForRouting.features[0]
        bookingViewModel.distance = (feature.properties.distance/1000.0).roundToInt()
        val listPoints = feature.geometry.coordinates[0].map { position ->
            LatLng(position[1],position[0])
        }
        PolylineOptions()
            .apply {
                this.addAll(listPoints)
                this.width(10f)
                this.color(Color.RED)
                this.geodesic(true)
            }
            .let { polyline -> map.addPolyline(polyline) }
        val origin = LatLng(feature.properties.waypoints[0].location[1], feature.properties.waypoints[0].location[0])
        val destination = LatLng(feature.properties.waypoints[1].location[1], feature.properties.waypoints[1].location[0])
        val bounds = LatLngBounds.Builder()
        addMarker(bounds,origin,"origin")
        addMarker(bounds,destination,"destination")
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng((origin.latitude+destination.latitude)/2, (origin.longitude+destination.longitude)/2,), 14f))
    }
    private fun addMarker(bounds: LatLngBounds.Builder, latLng: LatLng, marker: String){
        map.addMarker( MarkerOptions().position(latLng).title(marker) )
        bounds.include(latLng)
    }



    private var isFinishMoving: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            bookingViewModel.clear()
            Log.e("---------------","done")
            unregisterReceiver(updateDriverLocation)
            unregisterReceiver(this)
            finishAffinity()
            startActivity(Intent(this@RoutingActivity, MainActivity::class.java).apply { this.putExtra(Constant.CONGRATULATE,"CONGRATULATE") })
        }
    }

    private val updateDriverLocation = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            marker?.remove()
            marker = null
            val currentLocationDriver: CurrentLocationDriver? = Gson().fromJson(p1?.getStringExtra(Constant.UPDATE_LOCATION_DRIVER_STRING), CurrentLocationDriver::class.java)
            val bitmapDescriptor: BitmapDescriptor? = Constant.convertDrawableToBitMap(getDrawable(R.drawable.navigation_puck_icon_24))?.let { it1 -> BitmapDescriptorFactory.fromBitmap(it1) }
            Log.e("*******",currentLocationDriver.toString())
            val location = LatLng(currentLocationDriver!!.driverLocation.latitude, currentLocationDriver.driverLocation.longitude)
            marker = map.addMarker( MarkerOptions().apply {
                this.position(location)
                this.icon(bitmapDescriptor)
            })
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location,18f))
        }
    }


}