package com.example.user.di

import com.example.user.data.api.RouteNavigationApi
import com.example.user.data.api.UserApi
import com.example.user.data.repository.route.RouteNavigationCacheDataResource
import com.example.user.data.repository.route.RouteNavigationRemoteDataSource
import com.example.user.data.repository.route.RouteNavigationRepositoryImpl
import com.example.user.data.repository.route.impl.RouteNavigationCacheDataResourceImpl
import com.example.user.data.repository.route.impl.RouteNavigationRemoteDataSourceImpl
import com.example.user.domain.repository.RouteNavigationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun providesGoogleMapApi(): RouteNavigationApi =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://maps.googleapis.com/")
            .build()
            .create(RouteNavigationApi::class.java)

    @Provides
    @Singleton
    fun providesUserApi(): UserApi =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://maps.googleapis.com/")
            .build()
            .create(UserApi::class.java)

    @Provides
    fun providesRouteNavigationRepository(
        routeNavigationCacheDataResource: RouteNavigationCacheDataResource,
        routeNavigationRemoteDataSource: RouteNavigationRemoteDataSource
    ): RouteNavigationRepository =
        RouteNavigationRepositoryImpl(
            routeNavigationCacheDataResource,
            routeNavigationRemoteDataSource
        )

    @Provides
    fun providesRouteNavigationCacheDataResource(): RouteNavigationCacheDataResource =
        RouteNavigationCacheDataResourceImpl()

    @Provides
    fun providesRouteNavigationRemoteDataSource(
        routeNavigationApi: RouteNavigationApi,
    ): RouteNavigationRemoteDataSource = RouteNavigationRemoteDataSourceImpl(routeNavigationApi)
}