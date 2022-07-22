package com.example.user.di

import android.content.Context
import androidx.room.Room
import com.example.user.data.api.RouteNavigationApi
import com.example.user.data.api.AuthenticationApi
import com.example.user.data.api.Check
import com.example.user.data.dao.UserDao
import com.example.user.data.database.BackEndDatabase
import com.example.user.data.repository.route.RouteNavigationCacheDataResource
import com.example.user.data.repository.route.RouteNavigationRemoteDataSource
import com.example.user.data.repository.route.RouteNavigationRepositoryImpl
import com.example.user.data.repository.route.impl.RouteNavigationCacheDataResourceImpl
import com.example.user.data.repository.route.impl.RouteNavigationRemoteDataSourceImpl
import com.example.user.domain.repository.RouteNavigationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
            .baseUrl("https://maps.googleapis.com")
            .build()
            .create(RouteNavigationApi::class.java)

//    @Provides
//    @Singleton
//    fun providesCheck():

    @Provides
    @Singleton
    fun providesBackendApi(
        routeNavigationApi: RouteNavigationApi
    ): AuthenticationApi
        {
            val logging= HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

//            val check = Check(routeNavigationApi)
            val xclient= OkHttpClient.Builder().apply {
                this.addInterceptor(logging)
            }
            return Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.1.11:8080")
//                .baseUrl("http://192.168.223.107:8080")
                .client(xclient.build())
                .build()
                .create(AuthenticationApi::class.java)
        }

    @Provides
    @Singleton
    fun providesBackendDatabase(
        @ApplicationContext context: Context
    ): BackEndDatabase =
        Room.databaseBuilder(
            context,
            BackEndDatabase::class.java,
            "BackEnd Database"
        ).fallbackToDestructiveMigration().build()

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

    @Provides
    @Singleton
    fun providesUserDao(backEndDatabase: BackEndDatabase): UserDao = backEndDatabase.userDao()
}