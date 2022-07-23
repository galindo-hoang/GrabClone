package com.example.user.data.repository.authentication

import com.example.user.data.dto.UserDto
import com.example.user.data.model.authentication.TokenAuthentication
import com.example.user.data.model.authentication.User

interface AuthenticationCacheDataResource {
    fun getUser(): UserDto?
    fun getToken(): TokenAuthentication?
    fun updateUser(userDto: UserDto?)
    fun updateToken(tokenAuthentication: TokenAuthentication)

    fun getRefreshToken(): String
    fun getAccessToken(): String
    fun getPhoneNumber(): String
    fun getUserName(): String
}