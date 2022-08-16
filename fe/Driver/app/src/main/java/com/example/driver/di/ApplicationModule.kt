package com.example.driver.di

import android.content.Context
import androidx.room.Room
import com.example.driver.data.api.AuthenticationApi
import com.example.driver.data.api.BookingApi
import com.example.driver.data.api.CheckAccessTokenInterceptor
import com.example.driver.data.api.RenewAccessTokenApi
import com.example.driver.data.dao.TokenDao
import com.example.driver.data.dao.UserDao
import com.example.driver.data.database.BackEndDatabase
import com.example.driver.data.repository.authentication.AuthenticationCacheDataResource
import com.example.driver.data.repository.authentication.AuthenticationLocalDataResource
import com.example.driver.data.repository.authentication.AuthenticationRemoteDataResource
import com.example.driver.data.repository.authentication.AuthenticationRepositoryImpl
import com.example.driver.data.repository.authentication.impl.AuthenticationCacheDataResourceImpl
import com.example.driver.data.repository.authentication.impl.AuthenticationLocalDataResourceImpl
import com.example.driver.data.repository.authentication.impl.AuthenticationRemoteDataResourceImpl
import com.example.driver.data.repository.booking.BookingRemoteDataResource
import com.example.driver.data.repository.booking.BookingRepositoryImpl
import com.example.driver.data.repository.booking.impl.BookingRemoteDataResourceImpl
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.domain.repository.BookingRepository
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

//    @Provides
//    @Singleton
//    fun providesGoogleMapApi(): RouteNavigationApi =
//        Retrofit
//            .Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl("https://maps.googleapis.com")
//            .build()
//            .create(RouteNavigationApi::class.java)

//    @Provides
//    @Singleton
//    fun providesCheck():

    @Provides
    @Singleton
    fun providesAuthenticationApi(): AuthenticationApi
        {
            val logging= HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }
            return Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://34.142.192.100:8085")
//                .baseUrl("http://192.168.1.75:8085")
//                .baseUrl("http://192.168.223.107:8080")
                .client(
                    OkHttpClient.Builder().apply { this.addInterceptor(logging) }.build()
                )
                .build()
                .create(AuthenticationApi::class.java)
        }

    @Provides
    @Singleton
    fun providesRenewAccessTokenApi(): RenewAccessTokenApi =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://34.142.192.100:8085")
//            .baseUrl("http://192.168.1.75:8085")
            .build()
            .create(RenewAccessTokenApi::class.java)

    @Provides
    @Singleton
    fun providesBookingApi(checkAccessTokenInterceptor: CheckAccessTokenInterceptor): BookingApi =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://34.142.192.100:8085")
//            .baseUrl("http://192.168.1.75:8085")
            .client(
                OkHttpClient.Builder()
                    .apply { this.addInterceptor(checkAccessTokenInterceptor) }
                    .build()
            )
            .build()
            .create(BookingApi::class.java)

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

//    @Provides
//    fun providesRouteNavigationRepository(
//        routeNavigationCacheDataResource: RouteNavigationCacheDataResource,
//        routeNavigationRemoteDataSource: RouteNavigationRemoteDataSource
//    ): RouteNavigationRepository =
//        RouteNavigationRepositoryImpl(
//            routeNavigationCacheDataResource,
//            routeNavigationRemoteDataSource
//        )

//    @Provides
//    fun providesRouteNavigationCacheDataResource(): RouteNavigationCacheDataResource =
//        RouteNavigationCacheDataResourceImpl()

//    @Provides
//    fun providesRouteNavigationRemoteDataSource(
//        routeNavigationApi: RouteNavigationApi,
//    ): RouteNavigationRemoteDataSource = RouteNavigationRemoteDataSourceImpl(routeNavigationApi)

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
        bookingApi: BookingApi
    ): BookingRemoteDataResource = BookingRemoteDataResourceImpl(bookingApi)

    @Provides
    @Singleton
    fun providesBookingRepository(
        bookingRemoteDataResource: BookingRemoteDataResource
    ): BookingRepository = BookingRepositoryImpl(bookingRemoteDataResource)
}