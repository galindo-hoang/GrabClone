package com.example.user.presentation.booking

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.BuildConfig
import com.example.user.R
import com.example.user.data.dto.CurrentLocationDriver
import com.example.user.data.dto.LatLong
import com.example.user.data.model.place.Address
import com.example.user.databinding.ActivitySearchingRouteBinding
import com.example.user.domain.usecase.GetRouteNavigationUseCase
import com.example.user.presentation.BaseActivity
import com.example.user.presentation.booking.adapter.AddressAdapter
import com.example.user.utils.Constant
import com.example.user.utils.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt


@SuppressLint("UseCompatLoadingForDrawables")
@AndroidEntryPoint
class SearchingRouteActivity : BaseActivity() {
    @Inject
    lateinit var getRouteNavigationUseCase: GetRouteNavigationUseCase
    @Inject
    lateinit var bookingViewModel: BookingViewModel
    lateinit var origin: LatLng
    lateinit var destination: LatLng
    private val addressAdapter = AddressAdapter()
    private var marker: Marker? = null
    private var isOrigin: Boolean? = null
    private lateinit var binding: ActivitySearchingRouteBinding
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchingRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(!Places.isInitialized())
            Places.initialize(this, BuildConfig.GOOGLE_MAP_API)
        setupRecyclerView()
        registerClickListener()
        registerViewChangeListener()
    }


    private fun setupRecyclerView() {
        addressAdapter.setOnClickListener { address ->
            if(isOrigin == true){
                bookingViewModel.origin = address
                binding.etOrigin.setText(address.name)
            }
            if(isOrigin == false){
                bookingViewModel.destination = address
                binding.etDestination.setText(address.name)
            }
            isOrigin = null
            binding.rvAddress.visibility = View.GONE
        }
        binding.rvAddress.adapter = addressAdapter
        binding.rvAddress.layoutManager = LinearLayoutManager(this)

    }

    override fun onStart() {
        super.onStart()
        isBooking()
    }

    private fun isBooking() {
        if(bookingViewModel.isBooking){
            val listeningDriver = object : BroadcastReceiver() {
                override fun onReceive(p0: Context?, p1: Intent?) {
                    this@SearchingRouteActivity.registerFinishMoving()
                    this@SearchingRouteActivity.registerLocationDriver(updateDriverLocation)
                    unregisterReceiver(this)
                }
            }
            registerReceiver(listeningDriver, IntentFilter(Constant.HAVE_DRIVER))
            bookingViewModel.isBooking = false
        }
    }


    val updateDriverLocation = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            marker?.remove()
            marker = null
            var currentLocationDriver: CurrentLocationDriver? = null
            var bitmapDescriptor: BitmapDescriptor? = null
            CoroutineScope(Dispatchers.IO).launch {
                currentLocationDriver = Gson().fromJson(
                    p1?.getStringExtra(Constant.UPDATE_LOCATION_DRIVER_STRING),
                    CurrentLocationDriver::class.java
                )
                bitmapDescriptor = Constant.convertDrawableToBitMap(
                    getDrawable(R.drawable.navigation_puck_icon_24)
                ) ?.let { it1 -> BitmapDescriptorFactory.fromBitmap(it1) }
            }
            marker = map.addMarker(
                MarkerOptions().apply {
                    this.position(
                        LatLng(
                            currentLocationDriver!!.driverLocation.latitude,
                            currentLocationDriver!!.driverLocation.longitude
                        )
                    )
                    this.icon(bitmapDescriptor)
                }
            )
        }
    }


    private fun registerClickListener() {
        binding.btnSearchingCar.setOnClickListener {
            if(binding.btnSearchingCar.text.toString() == Constant.SEARCHING_ROUTE)
                bookingViewModel.searchingRoute()
            else
                startActivity(Intent(this,MethodBookingActivity::class.java))
        }
    }

    var delay: Long = 1500 // 1.5 seconds after user stops typing
    var lastEditText: Long = 0
    var handler: Handler = Handler(Looper.myLooper()!!)
    private val inputOriginChecker = object : Runnable {
        override fun run() {
            if (System.currentTimeMillis() > lastEditText + delay - 500) {
                if(isOrigin == true) {
                    bookingViewModel.getListAddress(binding.etOrigin.text.toString())
                }
            }else{
                handler.removeCallbacks(this)
            }
        }
    }
    private val inputDestinationChecker = object : Runnable {
        override fun run() {
            if (System.currentTimeMillis() > lastEditText + delay - 500) {
                if(isOrigin == false)
                    bookingViewModel.getListAddress(binding.etDestination.text.toString())
            }else{
                handler.removeCallbacks(this)
            }
        }
    }

    private fun registerViewChangeListener(){
        binding.etOrigin.addTextChangedListener{
            if(it?.length!! >= 4){
                isOrigin = true
                lastEditText = System.currentTimeMillis()
                handler.postDelayed(inputOriginChecker,delay)
            }
        }
        binding.etDestination.addTextChangedListener{
            if(it?.length!! >= 4){
                isOrigin = false
                lastEditText = System.currentTimeMillis()
                handler.postDelayed(inputDestinationChecker,delay)
            }
        }

        bookingViewModel.listAddress.observe(this) {
            when(it.status){
                Status.LOADING -> this.showProgressDialog()
                Status.SUCCESS -> {
                    this.hideProgressDialog()
                    Log.e("---------",it.data.toString())
                    addressAdapter.setList(it.data?.features!!.map { feature ->
                        Address(
                            LatLong(feature.properties.lat,feature.properties.lon),
                            feature.properties.formatted
                        )
                    })
                    binding.rvAddress.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    this.hideProgressDialog()
                    Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                }
            }
        }

        bookingViewModel.routes.observe(this) {
            when(it.status) {
                Status.LOADING -> this.showProgressDialog()
                Status.ERROR -> {
                    this.hideProgressDialog()
                    Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    this.hideProgressDialog()

                    val feature = it.data!!.features[0]
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
                    origin = LatLng(
                        feature.properties.waypoints[0].location[1],
                        feature.properties.waypoints[0].location[0]
                    )
                    destination = LatLng(
                        feature.properties.waypoints[1].location[1],
                        feature.properties.waypoints[1].location[0]
                    )
                    val bounds = LatLngBounds.Builder()
                    addMarker(bounds,origin,"Marker 1")
                    addMarker(bounds,destination,"Marker 2")
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            (origin.latitude+destination.latitude)/2,
                            (origin.longitude+destination.longitude)/2,
                        ),
                        12f
                    ))
                    binding.btnSearchingCar.text = Constant.CONTINUE
//                    checkingUpdateLocationDriver()
                }
            }
        }
    }

//    private lateinit var listPoints: List<LatLng>
//    private fun checkingUpdateLocationDriver() {
//        CoroutineScope(Dispatchers.IO).launch {
//            repeat(listPoints.size) {
//                withContext(Dispatchers.Main) {
//                    marker = map.addMarker(
//                        MarkerOptions().apply {
//                            this.position(listPoints[it])
//                            this.icon(
//                                Constant.convertDrawableToBitMap(
//                                    getDrawable(R.drawable.navigation_puck_icon_24)
//                                ) ?.let { it1 -> BitmapDescriptorFactory.fromBitmap(it1) }
//                            )
//                        }
//                    )
//                }
//                delay(1000)
//                withContext(Dispatchers.Main) {
//                    marker?.remove()
//                }
//                marker = null
//            }
//        }
//    }

    private fun addMarker(bounds: LatLngBounds.Builder, latLng: LatLng, marker: String){
        map.addMarker(
            MarkerOptions().position(latLng)
                .title(marker)
        )
        bounds.include(latLng)
    }
}
