package com.example.kakaologinsample.data.datastore

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

/**
 * Data Store Configuration
 */

object UserPreferencesKeys {
    val USER_ID = longPreferencesKey("user_id")
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
}