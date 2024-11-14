package com.example.kakaologinsample.domain.usecase

import com.example.kakaologinsample.data.local.model.UserToken
import com.example.kakaologinsample.data.repository.UserTokenDbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserTokenDbUseCase @Inject constructor(
    private val userTokenDbRepository: UserTokenDbRepository
) {
    suspend fun getAllData() : Flow<List<UserToken>> = userTokenDbRepository.getAllData()
}