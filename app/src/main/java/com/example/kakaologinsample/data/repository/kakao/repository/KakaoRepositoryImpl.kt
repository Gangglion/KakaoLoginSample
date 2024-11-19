package com.example.kakaologinsample.data.repository.kakao.repository

import android.content.Context
import com.example.kakaologinsample.data.repository.kakao.mapper.toData
import com.example.kakaologinsample.data.repository.kakao.model.Token
import com.example.kakaologinsample.data.repository.kakao.model.UserInfo
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KakaoRepositoryImpl @Inject constructor() : KakaoRepository {
    override suspend fun loginUseKakao(context: Context): Flow<Result<Token?>> {
        return flow {
            try {
                val token: Token? = if(UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                    suspendCoroutine { continuation ->
                        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                            if(error != null) continuation.resumeWith(Result.failure(error))
                            else continuation.resume(token?.toData())
                        }
                    }
                } else {
                    suspendCoroutine { continuation ->
                        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                            if (error != null) continuation.resumeWith(Result.failure(error))
                            else continuation.resume(token?.toData())
                        }
                    }
                }
                emit(Result.success(token))
            } catch(e: Exception) {
                emit(Result.failure(e))
            }
        }
    }

    override suspend fun getUserInfo(): Flow<Result<UserInfo?>> {
        return flow {
            val userInfo = suspendCoroutine { continuation ->
                UserApiClient.instance.me { user, error ->
                    if(error != null) continuation.resumeWith(Result.failure(error))
                    else continuation.resume(user?.toData())
                }
            }
            emit(Result.success(userInfo))
        }
    }

    override suspend fun logout(): Flow<Result<Boolean>> {
        return flow {
            val result = suspendCoroutine { continuation ->
                UserApiClient.instance.logout { error ->
                    if(error != null) continuation.resumeWith(Result.failure(error))
                    else continuation.resume(true)
                }
            }
            emit(Result.success(result))
        }
    }

    override suspend fun unlink(): Flow<Result<Boolean>> {
        return flow {
            val result = suspendCoroutine { continuation ->
                UserApiClient.instance.unlink { error ->
                    if(error != null) continuation.resumeWith(Result.failure(error))
                    else continuation.resume(true)
                }
            }
            emit(Result.success(result))
        }
    }
}