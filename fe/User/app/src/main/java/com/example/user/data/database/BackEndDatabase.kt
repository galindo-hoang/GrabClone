package com.example.user.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.user.data.dao.UserDao
import com.example.user.data.model.authentication.User

@Database(entities = [User::class], version = 1)
abstract class BackEndDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}