package com.example.driver.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.driver.data.dao.TokenDao
import com.example.driver.data.dao.UserDao
import com.example.driver.data.model.authentication.TokenAuthentication
import com.example.driver.data.model.authentication.User

@Database(entities = [User::class, TokenAuthentication::class], version = 1)
abstract class BackEndDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tokenDao(): TokenDao
}