package com.example.user.data.repository.authentication

import com.example.user.data.dto.UserDto
import com.example.user.data.model.authentication.*
import retrofit2.Response

interface AuthenticationRemoteDataResource {
    suspend fun getResponseRegister(postValidateRegister: PostValidateRegister): Response<ResponseRegister>

    suspend fun getResponseValidateRegister(
        postValidateRegister: PostValidateRegister
    ): Response<ResponseValidateRegister>

    suspend fun getResponseLogin(postLogin: PostLogin): Response<ResponseLogin>
    suspend fun getAccessToken(refreshToken: String): Response<TokenAuthentication>
}