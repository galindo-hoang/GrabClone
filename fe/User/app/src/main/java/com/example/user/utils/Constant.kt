package com.example.user.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.util.*


object Constant {
    const val REQUEST_CURRENT_LOCATION = 1


    fun convertLatLongToAddress(context: Context, latLng: LatLng): String {
        val addressList: List<Address>? = Geocoder(
            context,
            Locale.getDefault()
        ).getFromLocation(latLng.latitude, latLng.longitude, 1)

        val sb = StringBuilder()
        if (addressList != null && addressList.isNotEmpty()) {
            val address: Address = addressList[0]
            for (i in 0..address.maxAddressLineIndex) {
                sb.append(address.getAddressLine(i)).append(",")
            }
            sb.deleteCharAt(sb.length - 1)
        }
        return sb.toString()
    }


    fun decodePoly(encoded: String): List<LatLng> {
        Log.i("Location", "String received: $encoded")
        val poly = ArrayList<LatLng>()
        var index = 0
        val len: Int = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = (encoded[index++] - 63).code
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = (encoded[index++] - 63).code
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)

            poly.add(p)
        }

        for (i in 0 until poly.size) {
            Log.i(
                "Location",
                "Point sent: Latitude: " + poly[i].latitude.toString() + " Longitude: " + poly[i].longitude
            )
        }
        return poly
    }
}