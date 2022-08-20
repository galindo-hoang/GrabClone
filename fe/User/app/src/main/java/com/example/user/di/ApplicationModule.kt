package com.example.user.di

import android.content.Context
import androidx.room.Room
import com.example.user.BuildConfig
import com.example.user.data.api.*
import com.example.user.data.api.interceptor.CheckAccessTokenInterceptor
import com.example.user.data.dao.TokenDao
import com.example.user.data.dao.UserDao
import com.example.user.data.database.BackEndDatabase
import com.example.user.data.repository.authentication.AuthenticationCacheDataResource
import com.example.user.data.repository.authentication.AuthenticationLocalDataResource
import com.example.user.data.repository.authentication.AuthenticationRemoteDataResource
import com.example.user.data.repository.authentication.AuthenticationRepositoryImpl
import com.example.user.data.repository.authentication.impl.AuthenticationCacheDataResourceImpl
import com.example.user.data.repository.authentication.impl.AuthenticationLocalDataResourceImpl
import com.example.user.data.repository.authentication.impl.AuthenticationRemoteDataResourceImpl
import com.example.user.data.repository.booking.BookingRemoteDataResource
import com.example.user.data.repository.booking.BookingRepositoryImpl
import com.example.user.data.repository.booking.impl.BookingRemoteDataResourceImpl
import com.example.user.data.repository.route.RouteNavigationCacheDataResource
import com.example.user.data.repository.route.RouteNavigationRemoteDataSource
import com.example.user.data.repository.route.RouteNavigationRepositoryImpl
import com.example.user.data.repository.route.impl.RouteNavigationCacheDataResourceImpl
import com.example.user.data.repository.route.impl.RouteNavigationRemoteDataSourceImpl
import com.example.user.di.annotation.AfterLoginApi
import com.example.user.di.annotation.BeforeLoginApi
import com.example.user.domain.repository.AuthenticationRepository
import com.example.user.domain.repository.BookingRepository
import com.example.user.domain.repository.RouteNavigationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

//    @Provides
//    @Singleton
//    fun providesGoogleMapApi(): RouteNavigationApi =
//        Retrofit
//            .Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl("https://maps.googleapis.com")
//            .build()
//            .create(RouteNavigationApi::class.java)

    @Provides
    @Singleton
    fun providesMapApi(): PlaceApi =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.geoapify.com")
            .build()
            .create(PlaceApi::class.java)

    @Provides
    @Singleton
    fun providesDirectionApi(): DirectionApi =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.mapbox.com")
            .build()
            .create(DirectionApi::class.java)

    @Provides
    @Singleton
    @BeforeLoginApi
    fun providesBeforeLoginApi(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.GCP)
            .build()

    @Provides
    @Singleton
    @AfterLoginApi
    fun providesAfterLoginApi(checkAccessTokenInterceptor: CheckAccessTokenInterceptor): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.GCP)
            .client(
                OkHttpClient.Builder()
                    .apply { this.addInterceptor(checkAccessTokenInterceptor) }
                    .build()
            )
            .build()

    @Provides
    @Singleton
    fun providesAuthenticationApi(@BeforeLoginApi retrofit: Retrofit): AuthenticationApi =
        retrofit.create(AuthenticationApi::class.java)

    @Provides
    @Singleton
    fun providesRenewAccessTokenApi(@BeforeLoginApi retrofit: Retrofit): RenewAccessTokenApi =
        retrofit.create(RenewAccessTokenApi::class.java)

    @Provides
    @Singleton
    fun providesBookingApi(@AfterLoginApi retrofit: Retrofit): BookingApi =
        retrofit.create(BookingApi::class.java)

    @Provides
    @Singleton
    fun providesFCMApi(@AfterLoginApi retrofit: Retrofit): FCMApi =
        retrofit.create(FCMApi::class.java)

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
        placeApi: PlaceApi,
        directionApi: DirectionApi
    ): RouteNavigationRemoteDataSource = RouteNavigationRemoteDataSourceImpl(placeApi,directionApi)

    @Provides
    @Singleton
    fun providesUserDao(backEndDatabase: BackEndDatabase): UserDao = backEndDatabase.userDao()
    @Provides
    @Singleton
    fun providesTokenDao(backEndDatabase: BackEndDatabase): TokenDao = backEndDatabase.tokenDao()

    @Singleton
    @Provides
    fun providesAuthenticationCacheDataResource(): AuthenticationCacheDataResource =
        AuthenticationCacheDataResourceImpl()

    @Provides
    @Singleton
    fun providesAuthenticationLocalDataResource(
        tokenDao: TokenDao,
        userDao: UserDao
    ): AuthenticationLocalDataResource = AuthenticationLocalDataResourceImpl(tokenDao,userDao)

    @Provides
    @Singleton
    fun providesAuthenticationRemoteDataResource(
        authenticationApi: AuthenticationApi,
        renewAccessTokenApi: RenewAccessTokenApi
    ): AuthenticationRemoteDataResource =
        AuthenticationRemoteDataResourceImpl(authenticationApi,renewAccessTokenApi)

    @Provides
    @Singleton
    fun providesAuthenticationRepository(
        authenticationCacheDataResource: AuthenticationCacheDataResource,
        authenticationLocalDataResource: AuthenticationLocalDataResource,
        authenticationRemoteDataResource: AuthenticationRemoteDataResource
    ): AuthenticationRepository =
        AuthenticationRepositoryImpl(
            authenticationCacheDataResource,
            authenticationLocalDataResource,
            authenticationRemoteDataResource
        )

    @Provides
    @Singleton
    fun providesBookingRemoteDataSource(
        bookingApi: BookingApi,
        fcmApi: FCMApi
    ): BookingRemoteDataResource = BookingRemoteDataResourceImpl(bookingApi,fcmApi)

    @Provides
    @Singleton
    fun providesBookingRepository(
        bookingRemoteDataResource: BookingRemoteDataResource
    ): BookingRepository = BookingRepositoryImpl(bookingRemoteDataResource)
}