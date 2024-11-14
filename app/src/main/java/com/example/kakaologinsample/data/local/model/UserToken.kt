package com.example.kakaologinsample.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserToken(
    @PrimaryKey val userId: Long,
    @ColumnInfo(name = "access_token") val accessToken: String?,
    @ColumnInfo(name = "refresh_token") val refreshToken: String
)