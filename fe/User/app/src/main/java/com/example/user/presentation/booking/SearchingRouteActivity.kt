package com.example.user.presentation.booking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.R
import com.example.user.data.api.AuthenticationApi
import com.example.user.data.dto.CurrentLocationDriver
import com.example.user.data.dto.LatLong
import com.example.user.data.model.place.Address
import com.example.user.data.model.route.Route
import com.example.user.databinding.ActivitySearchingRouteBinding
import com.example.user.domain.usecase.GetRouteNavigationUseCase
import com.example.user.presentation.BaseActivity
import com.example.user.presentation.booking.adapter.AddressAdapter
import com.example.user.utils.Constant
import com.example.user.utils.Status
import com.example.user.utils.TypeCar
import com.google.android.gms.maps.GoogleMap
import com.google.gson.Gson
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class SearchingRouteActivity : BaseActivity() {

    private lateinit var origin: Point
    private lateinit var destination: Point
    @Inject
    lateinit var getRouteNavigationUseCase: GetRouteNavigationUseCase
    @Inject
    lateinit var bookingViewModel: BookingViewModel
    @Inject
    lateinit var authenticationApi: AuthenticationApi

    private val addressAdapter = AddressAdapter()
    private var isOrigin: Boolean? = null
    private lateinit var binding: ActivitySearchingRouteBinding
    private lateinit var map: GoogleMap
    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this,getString(R.string.mapbox_api))
        binding = DataBindingUtil.setContentView(this, R.layout.activity_searching_route)
        binding.lifecycleOwner = this
        binding.viewModel = bookingViewModel
        setupMapbox(savedInstanceState)
        setupRecyclerView()
        registerClickListener()
        registerViewChangeListener()
    }

    private fun setupMapbox(savedInstanceState: Bundle?) {
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync{
            this.mapboxMap = it
        }
    }

    private fun initSource(loadedMapStyle: Style) {
        loadedMapStyle.addSource(GeoJsonSource(Constant.ROUTE_SOURCE_ID))
        val iconGeoJsonSource = GeoJsonSource(Constant.ICON_SOURCE_ID, FeatureCollection.fromFeatures(
            arrayOf<Feature>(
                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))
            )
        ))
        loadedMapStyle.addSource(iconGeoJsonSource)
    }

    // Add the route and marker icon layers to the map
    private fun initLayers(loadedMapStyle: Style) {
        val routeLayer = LineLayer(Constant.ROUTE_LAYER_ID, Constant.ROUTE_SOURCE_ID)

// Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
            lineCap(Property.LINE_CAP_ROUND),
            lineJoin(Property.LINE_JOIN_ROUND),
            lineWidth(5f),
            lineColor(Color.parseColor("#005096"))
        )
        loadedMapStyle.addLayer(routeLayer)

// Add the red marker icon image to the map
        loadedMapStyle.addImage(
            Constant.RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                AppCompatResources.getDrawable(this, R.drawable.ic_marker_red_24)
            )!!
        )

// Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(
            SymbolLayer(Constant.ICON_LAYER_ID, Constant.ICON_SOURCE_ID).withProperties(
                iconImage(Constant.RED_PIN_ICON_ID),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconOffset(arrayOf(0f, -9f))
            )
        )
    }


    private fun setupRecyclerView() {
        addressAdapter.setOnClickListener { address ->
            if(isOrigin == true){
                bookingViewModel.textOrigin.removeObservers(this)
                bookingViewModel.textOrigin.postValue(address.name)
                bookingViewModel.origin = address
            }
            if(isOrigin == false){
                bookingViewModel.textDestination.removeObservers(this)
                bookingViewModel.textDestination.postValue(address.name)
                bookingViewModel.destination = address
            }
            binding.rvAddress.visibility = View.INVISIBLE
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

    }

    private fun registerViewChangeListener(){
        bookingViewModel.textOrigin.observe(this) {
            if(it.length >= 4){
                bookingViewModel.getListAddress(it)
            }
        }
        bookingViewModel.textDestination.observe(this) {
            if(it.length >= 4){
                bookingViewModel.getListAddress(it)
            }
        }

        bookingViewModel.listAddress.observe(this) {
            when(it.status){
                Status.LOADING -> this.showProgressDialog()
                Status.SUCCESS -> {
                    this.hideProgressDialog()
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
        bookingViewModel.continuation.observe(this) {
            if(it) startActivity(Intent(this,MethodBookingActivity::class.java))
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
                    mapboxMap.setStyle(Style.MAPBOX_STREETS
                    ) { style ->
                        origin = Point.fromLngLat(
                            106.665290, 10.838678
                        )
                        destination = Point.fromLngLat(
                            106.680264,10.799194
                        )
                        initSource(style)
                        initLayers(style)
                        showRoute(it.data!!)
                    }
                    bookingViewModel.labelButton.postValue(Constant.CONTINUE)
                }
            }
        }
    }

    private fun showRoute(data: List<Route>) {
        mapboxMap.getStyle { style ->
            val source: GeoJsonSource = style.getSourceAs(Constant.ROUTE_SOURCE_ID)!!
            source.setGeoJson(LineString.fromPolyline(
                data[0].geometry.toString(),
                PRECISION_6)
            )
        }
    }

//    private fun checkingUpdateLocationDriver() {
//        val long = 106.676130
//        var lat = 10.809852
//        var marker = map.addMarker(MarkerOptions().position(LatLng(lat,long)))
//        repeat((1..5).count()) {
//            runBlocking {
//                marker?.remove()
//                delay(3000)
//                lat += 0.01
//            }
//            marker = map.addMarker(MarkerOptions().position(LatLng(lat,long)))
//        }
//    }
}
