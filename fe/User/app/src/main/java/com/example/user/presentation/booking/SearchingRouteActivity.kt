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
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.BuildConfig
import com.example.user.R
import com.example.user.data.dto.CurrentLocationDriver
import com.example.user.data.dto.LatLong
import com.example.user.data.model.place.Address
import com.example.user.databinding.ActivitySearchingRouteBinding
import com.example.user.domain.usecase.GetRouteNavigationUseCase
import com.example.user.presentation.base.BaseActivity
import com.example.user.presentation.booking.adapter.AddressAdapter
import com.example.user.presentation.main.MainActivity
import com.example.user.presentation.service.MyFirebaseMessaging
import com.example.user.utils.Constant
import com.example.user.utils.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Runnable
import javax.inject.Inject
import kotlin.math.roundToInt


@SuppressLint("UseCompatLoadingForDrawables")
@AndroidEntryPoint
class SearchingRouteActivity : BaseActivity() {

    @Inject
    lateinit var getRouteNavigationUseCase: GetRouteNavigationUseCase
    @Inject
    lateinit var bookingViewModel: BookingViewModel
    private val addressAdapter = AddressAdapter()
    private var isOrigin: Boolean? = null
    private lateinit var binding: ActivitySearchingRouteBinding
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchingRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupGoogleMap()
        setupRecyclerView()
        registerClickListener()
        registerViewChangeListener()
    }


    private fun setupGoogleMap() {
        if(!Places.isInitialized())
            Places.initialize(this,BuildConfig.GOOGLE_MAP_API)
        (supportFragmentManager.findFragmentById(R.id.map_view_in_booking_activity)
                as SupportMapFragment).getMapAsync {
            this.map = it
        }
    }


    private fun setupRecyclerView() {
        addressAdapter.setOnClickListener { address ->
            if(isOrigin == true){
                bookingViewModel.origin = address
                binding.etOrigin.setText(address.name)
                handler.removeCallbacks(inputOriginChecker)
            }
            if(isOrigin == false){
                bookingViewModel.destination = address
                binding.etDestination.setText(address.name)
                handler.removeCallbacks(inputDestinationChecker)
            }
            isOrigin = null
            binding.rvAddress.visibility = View.GONE
        }
        binding.rvAddress.adapter = addressAdapter
        binding.rvAddress.layoutManager = LinearLayoutManager(this)

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
    private val inputOriginChecker = Runnable {
        if (System.currentTimeMillis() > lastEditText + delay - 500) {
            isOrigin = true
            bookingViewModel.getListAddress(binding.etOrigin.text.toString())
        }
    }
    private val inputDestinationChecker = Runnable {
        if (System.currentTimeMillis() > lastEditText + delay - 500) {
            isOrigin = false
            bookingViewModel.getListAddress(binding.etDestination.text.toString())
        }
    }

    private fun registerViewChangeListener(){
        binding.etOrigin.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                handler.removeCallbacks(inputOriginChecker)
            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().length >= 4){
                    lastEditText = System.currentTimeMillis()
                    handler.postDelayed(inputOriginChecker,delay)
                }
            }
        })
        binding.etDestination.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    handler.removeCallbacks(inputDestinationChecker)
                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().length >= 4){
                        lastEditText = System.currentTimeMillis()
                        handler.postDelayed(inputDestinationChecker,delay)
                    }
                }
            }
        )

        bookingViewModel.listAddress.observe(this) {
            if(it != null) {
                when(it.status){
                    Status.LOADING -> this.showProgressDialog()
                    Status.SUCCESS -> {
                        this.hideProgressDialog()
                        Log.e("---------", it.data.toString())
                        addressAdapter.setList(it.data?.features!!.map { feature -> Address(LatLong(feature.properties.lat,feature.properties.lon), feature.properties.formatted) })
                        binding.rvAddress.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        this.hideProgressDialog()
                        Toast.makeText(this, it.message,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        bookingViewModel.routes.observe(this) {
            if(it != null) {
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
                        val listPoints = feature.geometry.coordinates[0].map { position -> LatLng(position[1],position[0]) }
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
                        addMarker(bounds,origin,"Marker 1")
                        addMarker(bounds,destination,"Marker 2")
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng((origin.latitude+destination.latitude)/2, (origin.longitude+destination.longitude)/2,), 14f))
                        binding.btnSearchingCar.text = Constant.CONTINUE
                    }
                }
            }
        }
    }

    private fun addMarker(bounds: LatLngBounds.Builder, latLng: LatLng, marker: String){
        map.addMarker(
            MarkerOptions().position(latLng)
                .title(marker)
        )
        bounds.include(latLng)
    }

    override fun onDestroy() {
        bookingViewModel.clear()
        super.onDestroy()
    }
}
