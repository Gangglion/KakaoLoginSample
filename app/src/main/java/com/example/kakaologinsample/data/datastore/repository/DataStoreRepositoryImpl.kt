package com.example.kakaologinsample.data.datastore.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.kakaologinsample.data.datastore.UserPreferencesKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore("user_prefs")
class DataStoreRepositoryImpl @Inject constructor(
    @ApplicationContext private val mContext: Context
) : DataStoreRepository{
    override val userId: Flow<Long?>
        get() = mContext.dataStore.data
            .map { preferences ->
                preferences[UserPreferencesKeys.USER_ID]
            }
    override val accessToken: Flow<String?>
        get() = mContext.dataStore.data
            .map { preferences ->
                preferences[UserPreferencesKeys.ACCESS_TOKEN]
            }
    override val refreshToken: Flow<String?>
        get() = mContext.dataStore.data
            .map { preferences ->
                preferences[UserPreferencesKeys.REFRESH_TOKEN]
            }

    override suspend fun saveUserId(userId: Long) {
        mContext.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.USER_ID] = userId
        }
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        mContext.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.ACCESS_TOKEN] = accessToken
            preferences[UserPreferencesKeys.REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun clearAllPrefs() {
        mContext.dataStore.edit { preferences ->
            preferences.apply {
                remove(UserPreferencesKeys.USER_ID)
                remove(UserPreferencesKeys.ACCESS_TOKEN)
                remove(UserPreferencesKeys.REFRESH_TOKEN)
            }
        }
    }
}