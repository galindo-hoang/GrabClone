package com.example.user.data.repository.authentication.impl

import com.example.user.data.dto.UserDto
import com.example.user.data.model.authentication.TokenAuthentication
import com.example.user.data.repository.authentication.AuthenticationCacheDataResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationCacheDataResourceImpl @Inject constructor(): AuthenticationCacheDataResource {
    private var userDto: UserDto? = null
    private var token: TokenAuthentication? = null
    override fun getUser(): UserDto? = userDto
    override fun getToken(): TokenAuthentication? = token

    override fun updateUser(userDto: UserDto?) {
        this.userDto = userDto
    }
    override fun updateToken(tokenAuthentication: TokenAuthentication?){
        this.token = tokenAuthentication
    }

    override fun getRefreshToken(): String = token?.refreshToken ?: ""
    override fun updateRefreshToken(refreshToken: String) {
        token?.let { it.refreshToken = refreshToken }
    }

    override fun getAccessToken(): String = token?.accessToken ?: ""
    override fun updateAccessToken(accessToken: String) {
        token?.let { it.accessToken = accessToken }
    }

    override fun getPhoneNumber(): String = userDto?.phoneNumber ?: ""
    override fun updatePhoneNumber(phoneNumber: String) {
        userDto?.let { it.phoneNumber = phoneNumber }
    }

    override fun getUserName(): String = userDto?.username ?: ""
}