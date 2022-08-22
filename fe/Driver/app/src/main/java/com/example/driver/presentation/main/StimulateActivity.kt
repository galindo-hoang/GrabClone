package com.example.driver.presentation.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.example.driver.BuildConfig
import com.example.driver.R
import com.example.driver.data.dto.LatLong
import com.example.driver.databinding.ActivityStimulateBinding
import com.example.driver.domain.usecase.SendCurrentLocationUseCase
import com.example.driver.presentation.BaseActivity
import com.example.driver.utils.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject


//Point.fromLngLat(106.665290,10.838678),
//Point.fromLngLat(106.698471,10.771423)

@AndroidEntryPoint
class StimulateActivity: BaseActivity() {
    @Inject
    lateinit var sendCurrentLocationUseCase: SendCurrentLocationUseCase
    private var marker: Marker? = null
    private var origin: LatLng? = null
    private var destination: LatLng? = null
    private var listPoints : List<LatLng>? = null
    private lateinit var binding: ActivityStimulateBinding
    private lateinit var map: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityStimulateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupGoogleMap()
        registerViewChangeListener()
        registerClickListener()
    }

    private fun registerClickListener() {
        binding.btnDoneDriving.setOnClickListener {
            stimulateViewModel.doneDriving().observe(this) {
                when(it.status) {
                    Status.LOADING -> this.showProgressDialog()
                    Status.SUCCESS -> {
                        this.hideProgressDialog()
                        finishAffinity()
                        startActivity(Intent(this,MainActivity::class.java))
                    }
                    Status.ERROR -> {
                        this.hideProgressDialog()
                        Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setupGoogleMap() {
        if(!Places.isInitialized())
            Places.initialize(this,BuildConfig.GOOGLE_MAP_API)

        (supportFragmentManager.findFragmentById(R.id.mapView)
                as SupportMapFragment).getMapAsync {
            this.map = it
        }
    }

    private fun registerViewChangeListener() {
        stimulateViewModel.getNavigation().observe(this) {
            when(it.status) {
                Status.LOADING -> this.showProgressDialog()
                Status.ERROR -> {
                    this.hideProgressDialog()
                    Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    this.hideProgressDialog()

                    val feature = it.data!!.features[0]
                    listPoints = feature.geometry.coordinates[0].map { position ->
                        LatLng(position[1],position[0])
                    }
                    PolylineOptions()
                        .apply {
                            this.addAll(listPoints!!)
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
                    addMarker(bounds, origin!!,"Marker 1")
                    addMarker(bounds, destination!!,"Marker 2")
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            (origin!!.latitude+ destination!!.latitude)/2,
                            (origin!!.longitude+ destination!!.longitude)/2,
                        ),
                        12f
                    ))
                    stimulation()
                }
            }
        }
    }

    private fun stimulation() {
        CoroutineScope(Dispatchers.IO).launch {
            repeat(listPoints!!.size) {
                val drawable = getDrawable(R.drawable.navigation_puck_icon_24)
                val bitmap = drawable!!.toBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight
                )
                withContext(Dispatchers.Main) {
                    marker = map.addMarker(
                        MarkerOptions().apply {
                            this.position(listPoints!![it])
                            this.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                        }
                    )
                }
                sendCurrentLocationUseCase.invoke(
                    LatLong(listPoints!![it].latitude, listPoints!![it].longitude)
                )
                delay(1000)
                withContext(Dispatchers.Main) {
                    marker?.remove()
                }
                marker = null
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
}