package com.example.kakaologinsample.data.repository

import com.example.kakaologinsample.data.local.dao.UserTokenDao
import com.example.kakaologinsample.data.local.model.UserToken
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserTokenDbRepository @Inject constructor(
    private val userTokenDao: UserTokenDao
) {

    suspend fun getAllData(): Flow<List<UserToken>> {
        return userTokenDao.getAll()
    }
}