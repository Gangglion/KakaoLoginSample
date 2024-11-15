package com.example.kakaologinsample.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.kakaologinsample.data.datastore.UserPreferencesKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPrefDataStore @Inject constructor(
    @ApplicationContext private val mContext: Context
) {
    // Get User Id
    val userId: Flow<Long?> = mContext.dataStore.data
        .map { preferences ->
            preferences[UserPreferencesKeys.USER_ID]
        }

    // Get Access Token
    val accessToken: Flow<String?> = mContext.dataStore.data
        .map { preferences ->
            preferences[UserPreferencesKeys.ACCESS_TOKEN]
        }

    // Get Refresh Token
    val refreshToken: Flow<String?> = mContext.dataStore.data
        .map { preferences ->
            preferences[UserPreferencesKeys.REFRESH_TOKEN]
        }

    // Save UserId
    suspend fun saveUserId(userId: Long) {
        mContext.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.USER_ID] = userId
        }
    }

    // Save AccessToken and RefreshToken
    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        mContext.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.ACCESS_TOKEN] = accessToken
            preferences[UserPreferencesKeys.REFRESH_TOKEN] = refreshToken
        }
    }

    // Clear All Preferences
    suspend fun clearAllPrefs() {
        mContext.dataStore.edit { preferences ->
            preferences.apply {
                remove(UserPreferencesKeys.USER_ID)
                remove(UserPreferencesKeys.ACCESS_TOKEN)
                remove(UserPreferencesKeys.REFRESH_TOKEN)
            }
        }
    }
}