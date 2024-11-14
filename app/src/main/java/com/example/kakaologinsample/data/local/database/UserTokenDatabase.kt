package com.example.kakaologinsample.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kakaologinsample.data.local.dao.UserTokenDao
import com.example.kakaologinsample.data.local.model.UserToken

@Database(entities = [UserToken::class], version = 1)
abstract class UserTokenDatabase : RoomDatabase() {
    abstract fun userTokenDao(): UserTokenDao
}