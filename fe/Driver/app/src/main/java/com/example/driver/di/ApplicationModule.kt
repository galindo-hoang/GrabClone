package com.example.driver.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.driver.BuildConfig
import com.example.driver.data.api.*
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
import com.example.driver.di.annotation.AfterLoginApi
import com.example.driver.di.annotation.BeforeLoginApi
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.domain.repository.BookingRepository
import com.example.driver.presentation.BaseApplication
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