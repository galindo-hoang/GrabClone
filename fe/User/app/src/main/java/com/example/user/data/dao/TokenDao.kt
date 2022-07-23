package com.example.user.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.user.data.model.authentication.TokenAuthentication

@Dao
interface TokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToken(tokenAuthentication: TokenAuthentication)

//    @Update
//    suspend fun updateToken(tokenAuthentication: TokenAuthentication)

    @Query("select * from TokenAuthentication where username = :userName")
    suspend fun fetchTokenByUserName(userName: String): TokenAuthentication

    @Query("select * from TokenAuthentication where refreshToken = :refresh")
    suspend fun fetchTokenByRefreshToken(refresh: String): TokenAuthentication

    @Query("select * from TokenAuthentication")
    suspend fun fetchToken(): List<TokenAuthentication>

    @Query("delete from TokenAuthentication")
    suspend fun clearAll()
}