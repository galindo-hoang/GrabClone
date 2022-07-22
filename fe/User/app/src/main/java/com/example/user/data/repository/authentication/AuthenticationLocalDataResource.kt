package com.example.user.data.repository.authentication

import com.example.user.data.model.authentication.BodyRefreshToken
import com.example.user.data.model.authentication.TokenAuthentication
import com.example.user.data.model.authentication.User

interface AuthenticationLocalDataResource {
    suspend fun getUserByPhoneNumber(phoneNumber: String): User
    suspend fun getUserByUserName(userName: String): User
    suspend fun getTokenByUserName(userName: String): TokenAuthentication
    suspend fun getTokenByRefreshToken(refreshToken: String): TokenAuthentication
    suspend fun getToken(): List<TokenAuthentication>
    fun saveUser(user: User)
    fun saveToken(tokenAuthentication: TokenAuthentication)
    fun clearAllUser()
    fun clearAllToken()
}