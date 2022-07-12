package com.example.user.data.repository.route.impl

import android.content.res.Resources
import androidx.core.content.ContextCompat
import com.example.user.BuildConfig
import com.example.user.R
import com.example.user.data.api.RouteNavigationApi
import com.example.user.data.model.RouteNavigation
import com.example.user.data.repository.route.RouteNavigationRemoteDataSource
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouteNavigationRemoteDataSourceImpl @Inject constructor(
    private val routeNavigationApi: RouteNavigationApi
): RouteNavigationRemoteDataSource {
    override suspend fun getRoutes(
        origin: String,
        destination: String,
        mode: String
    ): Response<RouteNavigation> =
        routeNavigationApi.getRoutes(
            origin,
            destination,
            mode,
            BuildConfig.GOOGLE_MAP_API
        )
}