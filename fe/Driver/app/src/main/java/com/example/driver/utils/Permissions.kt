package com.example.driver.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat

object Permissions {
    fun isEnableLocation(context: Context): Boolean{
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun turnOnLocation(context: Context){
        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }


    fun checkPermissions(context: Context): Boolean {
        return (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    fun requestPermissions(activity: Activity){
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            Constant.REQUEST_CURRENT_LOCATION
        )
    }

    fun isNetworkAvailable(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                else -> false
            }
        }
        else {
            return connectivityManager.activeNetworkInfo != null &&
                    connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting
        }
    }
}