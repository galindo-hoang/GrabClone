package com.example.user.data.repository.authentication.impl

import com.example.user.data.model.authentication.TokenAuthentication
import com.example.user.data.model.authentication.User
import com.example.user.data.repository.authentication.AuthenticationCacheDataResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationCacheDataResourceImpl @Inject constructor(): AuthenticationCacheDataResource {
    private var user: User? = null
    private var token: TokenAuthentication? = null
    override fun getUser(): User? = user
    override fun getToken(): TokenAuthentication? = token

    override fun updateUser(user: User) {
        this.user = user
    }
    override fun updateToken(tokenAuthentication: TokenAuthentication){
        this.token = tokenAuthentication
    }

    override fun getRefreshToken(): String = token?.refreshToken ?: ""
    override fun getAccessToken(): String = token?.accessToken ?: ""
    override fun getPhoneNumber(): String = user?.phoneNumber ?: ""
    override fun getUserName(): String = user?.username ?: ""
}