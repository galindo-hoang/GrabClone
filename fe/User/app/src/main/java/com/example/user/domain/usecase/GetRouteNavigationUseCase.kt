package com.example.user.domain.usecase

import android.content.Context
import android.graphics.Point
import androidx.core.content.ContextCompat
import com.example.user.R
import com.example.user.data.model.Route
import com.example.user.domain.repository.RouteNavigationRepository
import com.example.user.utils.Constant.decodePoly
import com.example.user.utils.Response
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class GetRouteNavigationUseCase @Inject constructor(
    private val routeNavigationRepository: RouteNavigationRepository
) {
    suspend fun invoke(/*map: GoogleMap,*/origin: String,destination: String, mode: String): Response<List<Route>> {
        val routeNavigation = routeNavigationRepository.getRouteNavigation(origin, destination, mode)
        if(routeNavigation.routes.isNotEmpty() && routeNavigation.status == "200") {
            return Response.success(routeNavigation.routes)
//            var points: MutableList<LatLng>
//            var polylineOptions = PolylineOptions()
//            routeNavigation.routes.forEach { route ->
//                points = mutableListOf()
//                polylineOptions = PolylineOptions()
//                route.legs.forEach { leg ->
//                    leg.steps.forEach { step ->
//                        val latlngList = decodePoly(step.polyline.points)
//                        latlngList.forEach { latLng ->
//                            points.add(LatLng(latLng.latitude, latLng.longitude))
//                        }
//                    }
//                }
//                polylineOptions.addAll(points)
//                polylineOptions.width(10f)
//                polylineOptions.color(ContextCompat.getColor(context, R.color.green))
//                polylineOptions.geodesic(true)
//            }
//            polylineOptions.let { map.addPolyline(it) }
//            map.addMarker(MarkerOptions().position(LatLng(routeNavigation.routes[0].legs[0].start_location.lat,routeNavigation.routes[0].legs[0].start_location.lng)).title("Marker 1"))
//            map.addMarker(MarkerOptions().position(LatLng(routeNavigation.routes[0].legs[0].end_location.lat,routeNavigation.routes[0].legs[0].end_location.lng)).title("Marker 1"))
        }else return Response.error(null, routeNavigation.status)
    }
}