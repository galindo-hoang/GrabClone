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
//    val accountNonExpired: Boolean? = null,
//    val accountNonLocked: Boolean? = null,
//    val credentialsNonExpired: Boolean? = null,
//    val enabled: Boolean? = null,

    var password: String?,
    var username: String,
    @SerializedName("phonenumber")
    var phoneNumber: String?,

) {
    @Ignore
    var authorities: List<Authority> = listOf()
//    private lateinit var authority: String
//    init {
//        if(authorities.isNotEmpty()) {
//            authority = authorities[0].authority
//        }
//    }
}