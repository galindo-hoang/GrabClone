package com.example.user.data.repository.authentication.impl

import com.example.user.data.dao.TokenDao
import com.example.user.data.dao.UserDao
import com.example.user.data.model.authentication.TokenAuthentication
import com.example.user.data.model.authentication.User
import com.example.user.data.repository.authentication.AuthenticationLocalDataResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationLocalDataResourceImpl @Inject constructor(
    private val tokenDao: TokenDao,
    private val userDao: UserDao
): AuthenticationLocalDataResource {
    override suspend fun getUserByPhoneNumber(phoneNumber: String): User =
        userDao.fetchUserByPhoneNumber(phoneNumber)

    override suspend fun getUserByUserName(userName: String): User =
        userDao.fetchUserByUserName(userName)

    override suspend fun getTokenByUserName(userName: String): TokenAuthentication =
        tokenDao.fetchTokenByUserName(userName)

    override suspend fun getTokenByRefreshToken(refreshToken: String): TokenAuthentication =
        tokenDao.fetchTokenByRefreshToken(refreshToken)

    override suspend fun getToken(): List<TokenAuthentication> =
        tokenDao.fetchToken()

    override fun saveUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.saveUser(user)
        }
    }
    override fun saveToken(tokenAuthentication: TokenAuthentication) {
        CoroutineScope(Dispatchers.IO).launch {
            tokenDao.saveToken(tokenAuthentication)
        }
    }

    override fun clearAllUser() {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.clearAll()
        }
    }

    override fun clearAllToken() {
        CoroutineScope(Dispatchers.IO).launch {
            tokenDao.clearAll()
        }
    }
}