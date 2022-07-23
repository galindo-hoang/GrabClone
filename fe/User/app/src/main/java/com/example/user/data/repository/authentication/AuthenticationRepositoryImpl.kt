package com.example.user.data.repository.authentication

import com.example.user.data.dto.UserDto
import com.example.user.data.dto.ValidateOTP
import com.example.user.data.model.authentication.ResponseLogin
import com.example.user.data.model.authentication.SuccessBodyValidateOrRegister
import com.example.user.data.model.authentication.TokenAuthentication
import com.example.user.domain.repository.AuthenticationRepository
import com.example.user.utils.Constant.convertTimeLongToDateTime
import com.example.user.utils.Constant.getPayloadDataFromJWTAccessToken
import com.example.user.utils.Constant.getPayloadDataFromJWTRefreshToken
import okhttp3.RequestBody
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationCacheDataResource: AuthenticationCacheDataResource,
    private val authenticationLocalDataResource: AuthenticationLocalDataResource,
    private val authenticationRemoteDataResource: AuthenticationRemoteDataResource
): AuthenticationRepository {
    override suspend fun postRequestRegister(
        userDto: UserDto
    ): Response<SuccessBodyValidateOrRegister> =
        authenticationRemoteDataResource.getResponseRegister(userDto)

    override suspend fun postValidateRegister(
        validateOTP: ValidateOTP
    ): Response<SuccessBodyValidateOrRegister> =
        authenticationRemoteDataResource.getResponseValidateRegister(validateOTP)

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
        var localToken: List<TokenAuthentication> = listOf()
        if(accessToken.isEmpty()){
            localToken = authenticationLocalDataResource.getToken()
            if(localToken.isEmpty()){
                throw Exception("cant get token from local database")
            }else{
                accessToken = localToken[0].accessToken
            }
        }
        val currentDate = Date()
        val bodyAccessToken = getPayloadDataFromJWTAccessToken(accessToken)
        if(currentDate >= convertTimeLongToDateTime(bodyAccessToken.exp)){
            if(localToken.isEmpty()){
                localToken = authenticationLocalDataResource.getToken()
                if(localToken.isEmpty()) throw Exception("cant get token from local database")
            }
            val bodyRefreshToken = getPayloadDataFromJWTRefreshToken(localToken[0].refreshToken)
            if(currentDate >= convertTimeLongToDateTime(bodyRefreshToken.exp)){
                throw Exception("Refresh token is expired")
            }
            accessToken = getAccessTokenFromRemote(localToken[0].refreshToken)
            val newToken = TokenAuthentication(
                username = authenticationCacheDataResource.getUserName(),
                refreshToken = localToken[0].refreshToken,
                accessToken = accessToken)
            authenticationLocalDataResource.saveToken(newToken)
            authenticationCacheDataResource.updateToken(newToken)
        }
        return accessToken
    }
    private suspend fun getAccessTokenFromRemote(refreshToken: String): String{
        val response = authenticationRemoteDataResource.getAccessToken(refreshToken)
        return when(response.code()){
            200 -> {
                val body = response.body()
                body?.accessToken ?: throw Exception("Backend Error")
            }
            403 -> throw Exception("refresh token is expired")
            else -> throw Exception("cant connect to database")
        }
    }

    override fun updateAccessToken(accessToken: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun updateAccount(responseLogin: ResponseLogin): UserDto {
        val newToken = TokenAuthentication(
            username = responseLogin.user.username,
            refreshToken = responseLogin.refreshToken,
            accessToken = responseLogin.accessToken
        )
        val newUser = UserDto(
            password = responseLogin.user.password,
            username = responseLogin.user.username,
            phoneNumber = responseLogin.user.phoneNumber
        )
        authenticationCacheDataResource.updateToken(newToken)
        authenticationCacheDataResource.updateUser(newUser)
        authenticationLocalDataResource.saveToken(newToken)
        authenticationLocalDataResource.saveUser(responseLogin.user)
        return newUser
    }

    override suspend fun postAccountLogin(requestBody: RequestBody): Response<ResponseLogin> =
        authenticationRemoteDataResource.getResponseLogin(requestBody)

    override suspend fun getAccount(): UserDto {
        var userDto = authenticationCacheDataResource.getUser()
        if(userDto == null){
            val user = authenticationLocalDataResource.getAllUser()
            if(user.isEmpty()) throw Exception("cant get account from local database")
            userDto = UserDto(
                username = user[0].username,
                password = user[0].password,
                phoneNumber = user[0].phoneNumber
            )
            authenticationCacheDataResource.updateUser(userDto)
        }
        return userDto
    }

    override suspend fun clearAll() {
        authenticationLocalDataResource.clearAllToken()
        authenticationLocalDataResource.clearAllUser()
        authenticationCacheDataResource.updateUser(null)
        authenticationCacheDataResource.updateToken(null)
    }

}