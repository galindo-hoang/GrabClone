package com.example.user.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.user.data.model.authentication.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

//    @Update
//    suspend fun updateUser(user: User)
    @Query("select * from User")
    suspend fun fetchAllUser(): List<User>

    @Query("select * from User where phoneNumber = :number")
    suspend fun fetchUserByPhoneNumber(number: String): User

    @Query("select * from User where username = :userName")
    suspend fun fetchUserByUserName(userName: String): User

    @Query("select count(*) from User")
    suspend fun fetchCountUser(): Int

    @Query("delete from User")
    suspend fun clearAll()
}