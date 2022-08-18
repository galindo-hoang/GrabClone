package com.example.driver.data.repository.authentication

import com.example.driver.data.dto.UserDto
import com.example.driver.data.dto.ValidateOTP
import com.example.driver.data.model.authentication.*
import com.example.driver.data.model.converter.TokenConverter
import com.example.driver.data.model.mapper.UserMapper
import com.example.driver.domain.repository.AuthenticationRepository
import com.example.driver.exception.ExpiredRefreshTokenExceptionCustom
import com.example.driver.utils.Constant.convertTimeLongToDateTime
import com.example.driver.utils.Constant.getCurrentDate
import com.example.driver.utils.Constant.getPayloadDataFromJWTAccessToken
import com.example.driver.utils.Constant.getPayloadDataFromJWTRefreshToken
import okhttp3.RequestBody
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
// update remote CALL(->) local CALL(->) cache
// maybe don't need expose function get refreshToken
@Singleton
class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationCacheDataResource: AuthenticationCacheDataResource,
    private val authenticationLocalDataResource: AuthenticationLocalDataResource,
    private val authenticationRemoteDataResource: AuthenticationRemoteDataResource
): AuthenticationRepository {
    override suspend fun postRequestRegisterPhoneNumber(
        userDto: UserDto
    ): Response<BodyValidateOrRegister> =
        authenticationRemoteDataResource.getResponseRegisterPhoneNumber(userDto)

    override suspend fun postRequestRegisterSaveAccount(
        userDto: UserDto
    ): Response<BodyRegisterSaveAccount> =
        authenticationRemoteDataResource.getResponseRegisterSaveAccount(userDto)

    override suspend fun postValidateRegister(
        validateOTP: ValidateOTP
    ): Response<BodyValidateOrRegister> =
        authenticationRemoteDataResource.getResponseValidatePhoneNumber(validateOTP)

    private suspend fun getTokenFromLocal(userName: String): TokenAuthentication =
        authenticationLocalDataResource.getTokenByUserName(userName)
            ?: throw Exception("Cant get token from Local Database")

    override suspend fun getAccessToken(): String {
        var accessToken = authenticationCacheDataResource.getAccessToken()
        if(accessToken.isEmpty()){
            //if cant get user throw exception in function getAccount()
            try {
                this.getAccount().username.let {
                    authenticationCacheDataResource.updateToken(getTokenFromLocal(it!!))
                }
                accessToken = authenticationCacheDataResource.getAccessToken()
            }catch (e: Exception) { throw e }
        }
        val bodyAccessToken = getPayloadDataFromJWTAccessToken(accessToken)
        if(getCurrentDate() >= convertTimeLongToDateTime(bodyAccessToken.exp)){
            val refreshToken = authenticationCacheDataResource.getRefreshToken()
            val bodyRefreshToken = getPayloadDataFromJWTRefreshToken(refreshToken)
            if(getCurrentDate() >= convertTimeLongToDateTime(bodyRefreshToken.exp)){
                throw ExpiredRefreshTokenExceptionCustom(this.getAccount().username!!)
            }
            try {
                accessToken = getAccessTokenFromRemote(refreshToken)
                authenticationCacheDataResource.updateAccessToken(accessToken)
                authenticationLocalDataResource.saveToken(authenticationCacheDataResource.getToken()!!)
            } catch (e: Exception) { throw e }
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
            // never go to this branch
            403 -> throw Exception("refresh token is expired")
            else -> throw Exception("cant connect to database")
        }
    }
    override fun updateAccount(responseLogin: ResponseLogin): UserDto {
        updateAccountToLocal(responseLogin.user)
        updateTokenToLocal(TokenConverter.convertFromNetwork(responseLogin))
        return UserMapper.mapFromEntity(responseLogin.user)
    }

    private fun updateAccountToLocal(user: User) {
        authenticationLocalDataResource.saveUser(user)
        authenticationCacheDataResource.updateUser(UserMapper.mapFromEntity(user))
    }

    private fun updateTokenToLocal(tokenAuthentication: TokenAuthentication){
        authenticationCacheDataResource.updateToken(tokenAuthentication)
        authenticationLocalDataResource.saveToken(tokenAuthentication)
    }

    override suspend fun postAccountLogin(requestBody: RequestBody): Response<ResponseLogin> =
        authenticationRemoteDataResource.getResponseLogin(requestBody)

    override suspend fun getAccount(): UserDto {
        var userDto = authenticationCacheDataResource.getUser()
        if(userDto == null) {
            try { userDto = getAccountFromLocal() }
            catch (e: Exception) { throw e }
        }
        return userDto
    }

    override suspend fun getNumberAccount(): Int = authenticationLocalDataResource.getAllUser().size

    override suspend fun getNumberToken(): Int = authenticationLocalDataResource.getAllToken().size

    private suspend fun getAccountFromLocal(): UserDto {
        val users = authenticationLocalDataResource.getAllUser()
        if(users.isEmpty()) throw Exception("Please Login Account")
        authenticationCacheDataResource.updateUser(UserMapper.mapFromEntity(users[0]))
        return  authenticationCacheDataResource.getUser()!!
    }

    override suspend fun clearAll() {
        try {
            authenticationLocalDataResource.clearAllToken()
            authenticationLocalDataResource.clearAllUser()
            authenticationCacheDataResource.updateUser(null)
            authenticationCacheDataResource.updateToken(null)
        } catch (e: Exception) {
            throw Exception("cant log out")
        }
    }

}