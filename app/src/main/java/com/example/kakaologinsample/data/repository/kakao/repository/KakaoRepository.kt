package com.example.kakaologinsample.data.repository.kakao.repository

import android.content.Context
import com.example.kakaologinsample.data.repository.kakao.model.Token
import com.example.kakaologinsample.data.repository.kakao.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface KakaoRepository {
    suspend fun loginUseKakao(context: Context) : Flow<Result<Token?>>
    suspend fun getUserInfo() : Flow<Result<UserInfo?>>
    suspend fun logout() : Flow<Result<Boolean>>
    suspend fun unlink() : Flow<Result<Boolean>>
}