package com.example.kakaologinsample.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.kakaologinsample.data.local.model.UserToken
import kotlinx.coroutines.flow.Flow

@Dao
interface UserTokenDao {
    @Query("select * from USERTOKEN")
    suspend fun getAll(): Flow<List<UserToken>>

    @Query("select access_token from UserToken where userId = :userId")
    suspend fun getAccessToken(userId: Long)

    @Query("update userToken set access_token = :newAccessToken where userId = :userId")
    suspend fun updateAccessToken(userId: Long, newAccessToken: String)

    @Insert
    suspend fun insertAll(vararg userToken: UserToken) // vararg 는 가변인자, 매개변수의 갯수를 유동적으로 지정 가능. 1개만 넣어도 되고 2개 넣어도 되고 유동적임

    @Delete
    suspend fun delete(userToken: UserToken)
}