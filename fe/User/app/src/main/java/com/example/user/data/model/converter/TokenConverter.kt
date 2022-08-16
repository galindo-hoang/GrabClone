package com.example.user.data.model.converter

import com.example.user.data.model.authentication.ResponseLogin
import com.example.user.data.model.authentication.TokenAuthentication
import com.example.user.data.model.authentication.User
import com.example.user.utils.ObjectConverter

object TokenConverter: ObjectConverter<ResponseLogin,TokenAuthentication> {
    override fun convertFromNetwork(network: ResponseLogin): TokenAuthentication {
        return TokenAuthentication(
            username = network.user.username!!,
            accessToken = network.accessToken,
            refreshToken = network.refreshToken
        )
    }

    override fun convertToNetwork(domain: TokenAuthentication): ResponseLogin {
        return ResponseLogin(User(0,"","",""),"","")
    }
}