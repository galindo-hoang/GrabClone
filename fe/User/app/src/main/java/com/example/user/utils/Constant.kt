package com.example.user.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.example.user.data.model.authentication.BodyAccessToken
import com.example.user.data.model.authentication.BodyRefreshToken
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.util.*


object Constant {
    const val ROUTE_LAYER_ID = "route-layer-id"
    const val ROUTE_SOURCE_ID = "route-source-id"
    const val ICON_LAYER_ID = "icon-layer-id"
    const val ICON_SOURCE_ID = "icon-source-id"
    const val RED_PIN_ICON_ID = "red-pin-icon-id"
    const val FINISH_MOVING_STRING: String = "FINISH_MOVING_STRING"
    const val FINISH_MOVING: String = "FINISH_MOVING"
    const val UPDATE_LOCATION_DRIVER_STRING: String = "UPDATE_LOCATION_DRIVER_STRING"
    const val UPDATE_LOCATION_DRIVER: String = "UPDATE_LOCATION_DRIVER"
    const val HAVE_DRIVER_STRING: String = "HAVE_DRIVER_STRING"
    const val HAVE_DRIVER: String = "HAVE_DRIVER"
    const val SEARCHING_ROUTE: String = "Searching route"
    const val CONTINUE: String = "Continue"
    const val SERVICE_ACCESS_TOKEN: String = "SERVICE_ACCESS_TOKEN"
    const val SERVICE_ACCESS_TOKEN_BOOLEAN: String = "SERVICE_ACCESS_TOKEN"
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

    fun getPayloadDataFromJWTRefreshToken(token: String): BodyRefreshToken =
        Gson().fromJson(
            String(Base64.getDecoder().decode(token.split('.')[1])),
            BodyRefreshToken::class.java
        )
    fun getPayloadDataFromJWTAccessToken(token: String): BodyAccessToken =
        Gson().fromJson(
            String(Base64.getDecoder().decode(token.split('.')[1])),
            BodyAccessToken::class.java
        )

    fun getErrorBody(){

//            val type = object : TypeToken<ResponseValidateRegister>() {}.type
//            var errorResponse: ResponseValidateRegister? = Gson().fromJson(b.errorBody()!!.charStream(), type)
//            Log.e("-----", errorResponse.toString())
    }

    fun convertTimeLongToDateTime(time: Long): Date = Date(time * 1000)
    fun getCurrentDate() = Date()

    fun checkPhone(str: String): Boolean =
        str.matches("^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$".toRegex())
}