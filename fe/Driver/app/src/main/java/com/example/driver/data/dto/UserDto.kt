package com.example.driver.data.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
//    var authorities: String,
    var password: String?,
    var username: String?,
    @SerializedName("phonenumber")
    var phoneNumber: String?,
    @SerializedName("otp", alternate = ["onceTimePassword"])
    var otp: String? = null

)