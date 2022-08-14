package com.example.user.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.user.data.dao.TokenDao
import com.example.user.data.dao.UserDao
import com.example.user.data.model.authentication.TokenAuthentication
import com.example.user.data.model.authentication.User

@Database(entities = [User::class, TokenAuthentication::class], version = 1)
abstract class BackEndDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tokenDao(): TokenDao
}