package com.example.user.presentation.searching

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.user.R
import com.example.user.databinding.ActivitySearchingBinding
import com.example.user.presentation.BaseActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.AndroidEntryPoint
import java.text.Normalizer
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject


@AndroidEntryPoint
class SearchingActivity : BaseActivity() {
    @Inject
    lateinit var searchingViewModel: SearchingViewModel

    private lateinit var placesClient: PlacesClient
    private lateinit var autocompleteSessionToken: AutocompleteSessionToken

    private lateinit var binding: ActivitySearchingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_searching)
        binding.lifecycleOwner = this
        binding.viewModel = searchingViewModel

        val apiKey = resources.getString(R.string.map_api)
        if(!Places.isInitialized()){
            Places.initialize(this,apiKey)
        }
        placesClient = Places.createClient(this)
        autocompleteSessionToken = AutocompleteSessionToken.newInstance()

        searchingViewModel.origin.observe(this){
//            searchPlaces(it)

//            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.CONTENTS_FILE_DESCRIPTOR, fields)
//                .build(this)
//            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            when (resultCode) {
//                Activity.RESULT_OK -> {
//                    data?.let {
//                        val place = Autocomplete.getPlaceFromIntent(data)
//                        Log.i("TAG", "Place: ${place.name}, ${place.id}")
//                    }
//                }
//                AutocompleteActivity.RESULT_ERROR -> {
//                    // TODO: Handle the error.
//                    data?.let {
//                        val status = Autocomplete.getStatusFromIntent(data)
//                        Log.i("TAG", status.statusMessage ?: "")
//                    }
//                }
//                Activity.RESULT_CANCELED -> {
//                    // The user canceled the operation.
//                }
//            }
//            return
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//
//    private val AUTOCOMPLETE_REQUEST_CODE = 1
//
//    // Set the fields to specify which types of place data to
//    // return after the user has made a selection.
//    private val fields = listOf(Place.Field.ID, Place.Field.NAME)

//    private fun searchPlaces(str: String) {
//        val newRequest = FindAutocompletePredictionsRequest
//            .builder()
//            .setSessionToken(autocompleteSessionToken)
//            .setTypeFilter(TypeFilter.ESTABLISHMENT)
//            .setQuery(str)
//            .setCountries("VN")
//            .build()
//        placesClient.findAutocompletePredictions(newRequest)
//            .addOnSuccessListener { findAutocompletePredictionsResponse ->
//                val predictions = findAutocompletePredictionsResponse.autocompletePredictions
//                predictions
//                    .filter {
//                        Log.e("aaaaa",it.getFullText(null).toString())
//                        val a = removeAccent(it.getFullText(null).toString())
//                        Log.e("+++++",a)
//                        a.lowercase(Locale.getDefault()).contains(str)
//                    }
//                    .forEach { Log.e("=====", it.toString()) }
//            }.addOnFailureListener { e ->
//                if (e is ApiException) {
//                    Log.e("MainActivity", "Place not found: " + e.statusCode)
//                }
//            }
//    }
//
//    private fun removeAccent(s: String): String {
//        val temp: String = Normalizer.normalize(s, Normalizer.Form.NFD)
//        val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
//        return pattern.matcher(temp).replaceAll("")
//    }
//
//    private fun detailPlace(placeId: String) {
//        val placeFields: List<Place.Field> =
//            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
//        val request = FetchPlaceRequest.newInstance(placeId, placeFields)
//        placesClient.fetchPlace(request).addOnSuccessListener { fetchPlaceResponse ->
//            val place = fetchPlaceResponse.place
//            val latLng = place.latLng
//            if (latLng != null) {
//                Log.e("----", "LatLng : $latLng")
//            }
//        }.addOnFailureListener { e ->
//            if (e is ApiException) {
//                Log.e("MainActivity", "Place not found: " + e.message)
//                val statusCode = e.statusCode
//            }
//        }
//    }


}