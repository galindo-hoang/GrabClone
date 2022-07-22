package com.example.user.data.repository.authentication

import com.example.user.data.dto.UserDto
import com.example.user.data.model.authentication.*
import com.example.user.domain.repository.AuthenticationRepository
import com.example.user.utils.Constant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationCacheDataResource: AuthenticationCacheDataResource,
    private val authenticationLocalDataResource: AuthenticationLocalDataResource,
    private val authenticationRemoteDataResource: AuthenticationRemoteDataResource
): AuthenticationRepository {
    override suspend fun postRequestRegister(userDto: UserDto): ResponseRegister? {
        val user = User(
            username = userDto.username,
            password = userDto.password,
            phoneNumber = userDto.phoneNumber)
        authenticationCacheDataResource.updateUser(user)
        val response = authenticationRemoteDataResource.getResponseRegister(
            PostValidateRegister(0,user.phoneNumber)
        )
        return when(response.code()){
            200 -> response.body()
            400 -> {
                val type = object : TypeToken<ResponseValidateRegister>() {}.type
                Gson().fromJson(response.errorBody()!!.charStream(), type)
            }
            else -> throw Exception("cant connect to database")
        }
    }

    override suspend fun getRefreshToken(): String = getRefreshTokenFromCache()
    private suspend fun getRefreshTokenFromCache():String{
        var refreshToken = authenticationCacheDataResource.getRefreshToken()
        if(refreshToken == ""){
            val token = getRefreshTokenFromLocal()
            authenticationCacheDataResource.updateToken(token)
            refreshToken = token.refreshToken
        }
        return refreshToken
    }
    private suspend fun getRefreshTokenFromLocal(): TokenAuthentication{
        val localToken = authenticationLocalDataResource.getToken()
        if(localToken.isEmpty()){
            throw Exception("Cant get refresh token")
        }
        return localToken[0]
    }

    override suspend fun getAccessToken(): String {
        var accessToken = authenticationCacheDataResource.getAccessToken()
        if(accessToken.isEmpty()){
            val token = authenticationLocalDataResource.getToken()
            if(token.isEmpty()){
                throw Exception("cant get token from local database")
            }else{
                val bodyAccessToken = Constant.getPayloadDataFromJWTAccessToken(accessToken)
            }
        }
        return ""
    }

    override fun updateAccessToken(accessToken: String): String {
        TODO("Not yet implemented")
    }

    override fun updateAccount(postLogin: PostLogin): User {
        TODO("Not yet implemented")
    }

    override fun getAccount(): UserDto {
        TODO("Not yet implemented")
    }

}