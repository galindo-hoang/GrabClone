package com.example.user.utils

import android.util.Base64

class a {
}

fun main(){

    val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwaHVjIiwicm9sZXMiOlsiUk9MRV9EUklWRVIiXSwiaXNzIjoiaHR0cDovLzE5Mi4xNjguMS42Mzo4MDgwL2xvZ2luIiwiZXhwIjoxNjU4Njg0NTgxfQ.6XyDQGsVmVCKtRoW_jeJaxJRJr3RzEWfJWJEyC9oTKA"
//    val a = Gson().fromJson(
//        String(Base64.decodeBase64(token.split('.')[1].encodeToByteArray())),
//        BodyAccessToken::class.java
//    )
//    JWT.decode("eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IjEyMyJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaXNzIjoibmVmaWxpbSIsIm5hbWUiOiJKb2huIERvZSIsImFkbWluIjp0cnVlLCJpYXQiOjE1MTYyMzkwMjJ9.glaZCoqhNE7TiPLZl2hDK18yZGJUyVW0cE8pTM-zggyVfROiMPQJlImVcPSxTd50A8NRDOhoZwrqX04K4QS1bQ").tap {
//        println("the issuer is: ${it.issuer()}")
//        println("the subject is: ${it.subject()}")
//    }
    val a: ByteArray? =
        Base64.decode(token,Base64.DEFAULT)

    println(a)
}