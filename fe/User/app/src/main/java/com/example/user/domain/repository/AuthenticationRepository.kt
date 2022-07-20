package com.example.user.domain.repository

import com.example.user.data.model.authentication.PostValidateRegister
import com.example.user.data.model.authentication.ResponseRegister

interface AuthenticationRepository {
    suspend fun registerAccount(postValidateRegister: PostValidateRegister): ResponseRegister
    suspend fun validateAccount()
    suspend fun loginAccount(postValidateRegister: PostValidateRegister)
}