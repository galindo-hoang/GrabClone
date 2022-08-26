package com.example.user.data.model.authentication

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var password: String?,
    var username: String?,
    @SerializedName("phonenumber")
    var phoneNumber: String?,

)