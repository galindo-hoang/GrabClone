package com.example.driver.data.repository.authentication

import com.example.driver.data.model.authentication.TokenAuthentication
import com.example.driver.data.model.authentication.User

interface AuthenticationLocalDataResource {
    suspend fun getAllUser(): List<User>
    suspend fun getUserByPhoneNumber(phoneNumber: String): User
    suspend fun getUserByUserName(userName: String): User
    suspend fun getTokenByUserName(userName: String): TokenAuthentication?
    suspend fun getTokenByRefreshToken(refreshToken: String): TokenAuthentication
    suspend fun getAllToken(): List<TokenAuthentication>
    fun saveUser(user: User)
    fun saveToken(tokenAuthentication: TokenAuthentication)
    fun clearAllUser()
    fun clearAllToken()
}