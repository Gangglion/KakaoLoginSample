package com.example.kakaologinsample.data.kakao.repository

import android.content.Context
import com.example.kakaologinsample.data.kakao.model.Token
import com.example.kakaologinsample.data.kakao.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface KakaoRepository {
    suspend fun loginUseKakao(context: Context) : Flow<Result<Token?>>
    suspend fun getUserInfo() : Flow<Result<UserInfo?>>
    suspend fun logout() : Flow<Result<Boolean>>
    suspend fun unlink() : Flow<Result<Boolean>>
}