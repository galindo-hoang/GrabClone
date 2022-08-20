package com.example.user.utils

data class Response<T>(val status:Status, val data: T?, val responseCode: Int = 0, val message:String?){
    companion object {
        fun <T> success(data: T): Response<T> =
            Response(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, responseCode: Int, message: String): Response<T> =
            Response(status = Status.ERROR, responseCode = responseCode, data = data, message = message)

        fun <T> loading(data: T?): Response<T> =
            Response(status = Status.LOADING, data = data, message = null)
    }
}
