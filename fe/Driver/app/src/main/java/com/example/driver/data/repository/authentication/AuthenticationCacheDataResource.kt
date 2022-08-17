package com.example.driver.data.repository.authentication

import com.example.driver.data.dto.UserDto
import com.example.driver.data.model.authentication.TokenAuthentication

interface AuthenticationCacheDataResource {
    fun getUser(): UserDto?
    fun getToken(): TokenAuthentication?
    fun updateUser(userDto: UserDto?)
    fun updateToken(tokenAuthentication: TokenAuthentication?)

    fun getRefreshToken(): String
    fun updateRefreshToken(refreshToken: String)
    fun getAccessToken(): String
    fun updateAccessToken(accessToken: String)
    fun getPhoneNumber(): String
    fun updatePhoneNumber(phoneNumber: String)
    fun getUserName(): String
}