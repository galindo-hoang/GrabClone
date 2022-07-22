package com.example.user.presentation.searching

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.user.R
import com.example.user.data.api.AuthenticationApi
import com.example.user.data.model.authentication.PostValidateRegister
import com.example.user.data.model.authentication.ResponseValidateRegister
import com.example.user.databinding.ActivitySearchingBinding
import com.example.user.presentation.BaseActivity
import com.example.user.utils.Constant.decodePoly
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SearchingActivity : BaseActivity() {

    @Inject
    lateinit var searchingViewModel: SearchingViewModel
    @Inject
    lateinit var authenticationApi: AuthenticationApi

    private lateinit var placesClient: PlacesClient
    private lateinit var loadPlacesFromGoogleMap: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivitySearchingBinding
    private var isOrigin: Boolean? = null
    private lateinit var resultCheck: LatLng
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_searching)
        binding.lifecycleOwner = this
        binding.viewModel = searchingViewModel

//        val da = Gson().toJson(PostValidateRegister(12345,"+84906892676"))

        CoroutineScope(Dispatchers.IO).launch {
            val data = async {
                authenticationApi.postResponseValidateRegister(PostValidateRegister(476443,"+84906892676"))
            }
            val data1 = async {
                authenticationApi.postResponseRegister(PostValidateRegister(476443,"+84906892676"))
            }
            val a = data1.await()
            val b = data.await()




//            Log.e("-----", b.code().toString())
//            Log.e("-----", a.toString())
            Log.e("-----", b.toString())



//            Log.e("-----", b.message().toString())
        }

//        if(!Places.isInitialized())
//            Places.initialize(this,BuildConfig.GOOGLE_MAP_API)
//        placesClient = Places.createClient(this)
//
//        setupLoadPlaceFromGoogleMap()
//        setupHandleEventListener()
//        registerObserve()
    }

    private fun setupHandleEventListener() {
        binding.etDestination.setOnClickListener {
            isOrigin = false
            loadPlacesFromGoogleMap.launch(intentGoogleMap)
        }
        binding.etOrigin.setOnClickListener {
            isOrigin = true
            loadPlacesFromGoogleMap.launch(intentGoogleMap)
        }
        (supportFragmentManager.findFragmentById(R.id.map_view_in_searching_activity)
                as SupportMapFragment).getMapAsync {
                    this.map = it
        }
    }

    private fun registerObserve(){
        searchingViewModel.resultPlaceClient.observe(this){
            if(isOrigin == true) searchingViewModel.origin.postValue(it)
            else if (isOrigin == false) searchingViewModel.destination.postValue(it)
            isOrigin = null
        }
        searchingViewModel.routes.observe(this){ routes ->
            if(routes.isNotEmpty()){
                val polylineOptions = PolylineOptions()
                val points = mutableListOf<LatLng>()
                routes.forEach { route ->
                    route.legs.forEach { leg ->
                        leg.steps.forEach { step ->
                            points.addAll(decodePoly(step.polyline.points))
                        }
                    }
                }

                polylineOptions.addAll(points)
                polylineOptions.width(10f)
                polylineOptions.color(Color.RED)
                polylineOptions.geodesic(true)
                polylineOptions.let {
                    map.addPolyline(it)
                }
                val bounds = LatLngBounds.Builder()
                searchingViewModel.origin.observe(this){ latlng ->
                    resultCheck = LatLng(latlng.geometry.location.lat, latlng.geometry.location.lng)
                    map.addMarker(
                        MarkerOptions().position(resultCheck)
                            .title("Marker 1")
                    )
                    bounds.include(resultCheck)
                }
                searchingViewModel.destination.observe(this){ latlng ->
                    val result = LatLng(latlng.geometry.location.lat, latlng.geometry.location.lng)
                    map.addMarker(
                        MarkerOptions().position(result)
                            .title("Marker 2")
                    )
                    bounds.include(result)
                }
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
            }else{
                Log.e("5","hello")
            }
        }
    }

    private fun setupLoadPlaceFromGoogleMap(){
        loadPlacesFromGoogleMap =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
                when(result.resultCode) {
                    Activity.RESULT_OK -> {
                        result.data?.let {
                            val place = Autocomplete.getPlaceFromIntent(result.data!!)
                            place.id?.let { it1 -> searchingViewModel.getAddress(it1) }
                        }
                    }
                    AutocompleteActivity.RESULT_ERROR -> {
                        result.data?.let {
                            val status = Autocomplete.getStatusFromIntent(result.data!!)
                            Log.i("TAG", status.statusMessage ?: "")
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        // The user canceled the operation.
                    }
                }
            }
    }
}