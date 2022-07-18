package com.example.user.data.model.authentication

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val id: Long,
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    @Embedded
    val authorities: Authority,
    val credentialsNonExpired: Boolean,
    val enabled: Boolean,
    val password: String,
    val username: String,
    val phoneNumber: String,
)