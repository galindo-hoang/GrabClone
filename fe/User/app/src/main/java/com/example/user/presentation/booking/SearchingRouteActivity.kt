package com.example.user.presentation.booking

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.BuildConfig
import com.example.user.R
import com.example.user.data.api.AuthenticationApi
import com.example.user.data.dto.CurrentLocationDriver
import com.example.user.data.model.googlemap.ResultPlaceClient
import com.example.user.databinding.ActivitySearchingRouteBinding
import com.example.user.presentation.BaseActivity
import com.example.user.presentation.booking.adapter.AddressAdapter
import com.example.user.utils.Constant
import com.example.user.utils.Constant.decodePoly
import com.example.user.utils.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class SearchingRouteActivity : BaseActivity() {

    @Inject
    lateinit var bookingViewModel: BookingViewModel
    @Inject
    lateinit var authenticationApi: AuthenticationApi

    private val addressAdapter = AddressAdapter()
    private lateinit var placesClient: PlacesClient
    private lateinit var loadPlacesFromGoogleMap: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivitySearchingRouteBinding
    private var isOrigin: Boolean? = null
    private lateinit var map: GoogleMap
    private val intentGoogleMap by lazy {
        Autocomplete
            .IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN,
                listOf(Place.Field.ID, Place.Field.NAME)
            )
            .build(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_searching_route)
        binding.lifecycleOwner = this
        binding.viewModel = bookingViewModel
        if(!Places.isInitialized())
            Places.initialize(this, BuildConfig.GOOGLE_MAP_API)
        placesClient = Places.createClient(this)
        setupLoadPlaceFromGoogleMap()
        setupRecyclerView()
        registerClickListener()
        registerViewChangeListener()
    }

    private fun setupRecyclerView() {
        addressAdapter.setOnClickListener { address ->
            if(isOrigin == true){
                bookingViewModel.origin
            }
        }
        binding.rvAddress.adapter = addressAdapter
        binding.rvAddress.layoutManager = LinearLayoutManager(this)

    }

    override fun onStart() {
        super.onStart()
        isBooking()
        updateLocationDriver()
    }

    private fun isBooking() {
        if(bookingViewModel.isBooking){
            val listeningDriver = object : BroadcastReceiver() {
                override fun onReceive(p0: Context?, p1: Intent?) {
                    updateLocationDriver()
                    this@SearchingRouteActivity.onRegisterFinishMoving()
                    unregisterReceiver(this)
                }
            }
            registerReceiver(listeningDriver, IntentFilter(Constant.HAVE_DRIVER))
            bookingViewModel.isBooking = false
        }
    }

    private fun updateLocationDriver() {
        val updateLocation = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val currentLocationDriver = Gson().fromJson(
                    p1?.getStringExtra(Constant.UPDATE_LOCATION_DRIVER_STRING),
                    CurrentLocationDriver::class.java
                )
            }
        }
        this.onStartUpdateLocationDriver(updateLocation)
    }



    private fun registerClickListener() {
        binding.etDestination.setOnClickListener {
            isOrigin = false
            loadPlacesFromGoogleMap.launch(intentGoogleMap)
        }
        binding.etOrigin.setOnClickListener {
            isOrigin = true
            loadPlacesFromGoogleMap.launch(intentGoogleMap)
        }
        (supportFragmentManager.findFragmentById(R.id.map_view_in_booking_activity)
                as SupportMapFragment).getMapAsync {
                    this.map = it
        }
    }

    private fun registerViewChangeListener(){
        bookingViewModel.textOrigin.observe(this) {
            if(it.length >= 4){
                bookingViewModel.getListAddress(it)
            }
        }

        bookingViewModel.listAddress.observe(this) {
            when(it.status){
                Status.LOADING -> this.showProgressDialog()
                Status.SUCCESS -> {
                    this.hideProgressDialog()

                }
            }
        }

//        bookingViewModel.resultPlaceClient.observe(this){
//            when(it.status){
//                Status.LOADING -> this.showProgressDialog("Please waiting...")
//                Status.ERROR -> {
//                    this.hideProgressDialog()
//                    Toast.makeText(this, "Cant load data", Toast.LENGTH_LONG).show()
//                }
//                Status.SUCCESS -> {
//                    this.hideProgressDialog()
//                    if(isOrigin == true) {
//                        bookingViewModel.setOrigin(it.data)
//                        it.data?.let { rps -> binding.etOrigin.text = rps.formatted_address }
//                    }
//                    else if (isOrigin == false) {
//                        bookingViewModel.setDestination(it.data)
//                        it.data?.let { rps -> binding.etDestination.text = rps.formatted_address }
//                    }
//                    isOrigin = null
//                }
//            }
//        }
        bookingViewModel.routes.observe(this) { response ->
            when(response.status) {
                Status.LOADING -> this.showProgressDialog("Please waiting...")
                Status.ERROR -> {
                    this.hideProgressDialog()
                    Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    this.hideProgressDialog()
                    val routes = response.data!!
                    val points = mutableListOf<LatLng>()
                    routes.forEach { route ->
                        route.legs.forEach { leg ->
                            leg.steps.forEach { step ->
                                points.addAll(decodePoly(step.polyline.points))
                            }
                        }
                    }
                    PolylineOptions()
                        .apply {
                            this.addAll(points)
                            this.width(10f)
                            this.color(Color.RED)
                            this.geodesic(true)
                        }
                        .let { map.addPolyline(it) }
                    val bounds = LatLngBounds.Builder()
                    addMarker(bounds,bookingViewModel.origin!!,"Marker 1")
                    addMarker(bounds,bookingViewModel.destination!!,"Marker 2")
                    val point = Point()
                    windowManager.defaultDisplay.getSize(point)
                    map.animateCamera(
                        CameraUpdateFactory
                            .newLatLngBounds(
                                bounds.build(),
                                point.x,
                                550,
                                230
                            )
                    )
                    bookingViewModel.labelButton.postValue(Constant.CONTINUE)
                    checking()
                }
            }
        }
        bookingViewModel.continuation.observe(this) {
            if(it) startActivity(Intent(this,MethodBookingActivity::class.java))
        }
    }

    private fun checking() {
        val long = 106.676130
        var lat = 10.809852
        var marker = map.addMarker(MarkerOptions().position(LatLng(lat,long)))
        repeat((1..5).count()) {
            runBlocking {
                marker?.remove()
                delay(3000)
                lat += 0.01
            }
            marker = map.addMarker(MarkerOptions().position(LatLng(lat,long)))
        }
    }

    private fun setupLoadPlaceFromGoogleMap(){
        loadPlacesFromGoogleMap =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
                when(result.resultCode) {
                    Activity.RESULT_OK -> {
                        result.data?.let {
                            val place = Autocomplete.getPlaceFromIntent(result.data!!)
                            place.id?.let { it1 -> bookingViewModel.getAddress(it1) }
                        }
                    }
                    AutocompleteActivity.RESULT_ERROR -> {
                        result.data?.let {
                            val status = Autocomplete.getStatusFromIntent(result.data!!)
                            Log.i("TAG", status.statusMessage ?: "")
                        }
                    }
                    Activity.RESULT_CANCELED -> {}
                }
            }
    }

    private fun addMarker(bounds: LatLngBounds.Builder, place: ResultPlaceClient, marker: String){
        val latLong = LatLng(place.geometry.location.lat, place.geometry.location.lng)
        map.addMarker(
            MarkerOptions().position(latLong)
                .title(marker)
        )
        bounds.include(latLong)
    }
}
