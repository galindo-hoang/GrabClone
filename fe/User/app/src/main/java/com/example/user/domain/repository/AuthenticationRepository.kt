package com.example.user.domain.repository

import com.example.user.data.dto.UserDto
import com.example.user.data.model.authentication.PostLogin
import com.example.user.data.model.authentication.PostValidateRegister
import com.example.user.data.model.authentication.ResponseRegister
import com.example.user.data.model.authentication.User

interface AuthenticationRepository {
//    suspend fun registerAccount(postValidateRegister: PostValidateRegister): ResponseRegister
//    suspend fun validateAccount()
//    suspend fun loginAccount(postValidateRegister: PostValidateRegister)
suspend fun postRequestRegister(userDto: UserDto): ResponseRegister?
    suspend fun getRefreshToken(): String
    suspend fun getAccessToken(): String
    fun updateAccessToken(accessToken: String): String
    // login
    fun updateAccount(postLogin: PostLogin): User
    fun getAccount(): UserDto
}