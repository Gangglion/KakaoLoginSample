package com.example.kakaologinsample.data.datastore.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    val userId: Flow<Long?>
    val accessToken: Flow<String?>
    val refreshToken: Flow<String?>
    suspend fun saveUserId(userId: Long)
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clearAllPrefs()
}