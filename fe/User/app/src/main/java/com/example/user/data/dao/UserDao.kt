package com.example.user.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.user.data.model.authentication.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("select * from User where phoneNumber = :number")
    fun fetchUserByPhoneNumber(number: String): LiveData<User>

    @Query("delete from User")
    suspend fun clearAll()
}