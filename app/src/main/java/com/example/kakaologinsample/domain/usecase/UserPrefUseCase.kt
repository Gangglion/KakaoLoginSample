package com.example.kakaologinsample.domain.usecase

import com.example.kakaologinsample.data.repository.UserPrefDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPrefUseCase @Inject constructor(
  private val userPrefDataStore: UserPrefDataStore
) {
    fun getUserId(): Flow<Long?> = userPrefDataStore.userId

    fun getAccessToken(): Flow<String?> = userPrefDataStore.accessToken

    fun getRefreshToken(): Flow<String?> = userPrefDataStore.refreshToken

    suspend fun saveUserId(userId: Long) {
        userPrefDataStore.saveUserId(userId)
    }

    suspend fun saveUserToken(accessToken: String, refreshToken: String) {
        userPrefDataStore.saveTokens(accessToken, refreshToken)
    }

    suspend fun clearAllPrefs() {
        userPrefDataStore.clearAllPrefs()
    }
}