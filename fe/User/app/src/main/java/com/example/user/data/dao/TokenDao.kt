package com.example.user.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.user.data.model.authentication.TokenAuthentication

@Dao
interface TokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(tokenAuthentication: TokenAuthentication)

    @Update
    suspend fun updateToken(tokenAuthentication: TokenAuthentication)

    @Query("select * from TokenAuthentication where id = :Id")
    fun fetchTokenById(Id: Long): LiveData<TokenAuthentication>

    @Query("select * from TokenAuthentication where refreshToken = :refresh")
    fun fetchTokenByRefreshToken(refresh: String): LiveData<TokenAuthentication>

    @Query("delete * from TokenAuthentication")
    suspend fun clearAll()
}