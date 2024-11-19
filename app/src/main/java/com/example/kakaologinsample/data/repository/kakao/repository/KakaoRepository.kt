package com.example.kakaologinsample.data.repository.kakao.repository

import android.content.Context
import com.example.kakaologinsample.domain.model.Token
import com.example.kakaologinsample.domain.model.UserInfo
import com.example.kakaologinsample.domain.model.toData
import com.example.kakaologinsample.util.LogUtil
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KakaoRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun kakaoTalkLogin(activityContext: Context): Result<Token?> {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoTalk(activityContext) { token, error ->
                if(error != null) {
                    LogUtil.e("카카오톡을 통한 로그인 실패!!", error)
                    continuation.resume(Result.failure(error))
                }
                else continuation.resume(Result.success(token?.toData()))
            }
        }
    }

    suspend fun loginWithKakaoAccount(): Result<Token?> {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if(error != null) {
                    LogUtil.e("카카오계정을 통한 로그인 실패!!", error)
                    continuation.resume(Result.failure(error))
                }
                else continuation.resume(Result.success(token?.toData()))
            }
        }
    }

    suspend fun getUserInfo(): Result<UserInfo?> {
        return try {
            suspendCoroutine { continuation ->
                UserApiClient.instance.me { user, error ->
                    if(error != null) continuation.resume(Result.failure(error))
                    else continuation.resume(Result.success(user?.toData()))
                }
            }
        } catch(e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Boolean> {
        return try {
            Result.success(logoutFromKakao())
        } catch(e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun logoutFromKakao(): Boolean {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.logout { error ->
                if(error != null) continuation.resume(false)
                else continuation.resume(true)
            }
        }
    }

    suspend fun unlink(): Result<Boolean> {
        return try {
            Result.success(unlinkFromKakao())
        } catch(e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun unlinkFromKakao(): Boolean {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.unlink { error ->
                if(error != null) continuation.resume(false)
                else continuation.resume(true)
            }
        }
    }
}