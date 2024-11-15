package com.example.kakaologinsample.data.repository

import android.content.Context
import com.example.kakaologinsample.util.LogUtil
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KakaoAuthRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun kakaoAccountLogin() : Result<OAuthToken?> {
        return try {
            val token = loginWithKakaoAccount()
            Result.success(token)

        } catch(e : Exception) {
            Result.failure(e)
        }
    }

    private suspend fun loginWithKakaoAccount(): OAuthToken? {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if(error != null) {
                    LogUtil.e("카카오계정을 통한 로그인 실패!!", error)
                    continuation.resume(null)
                }
                else continuation.resume(token)
            }
        }
    }

    suspend fun getUserInfo(): Result<User?> {
        return try {
            val user = getInfoWithMe()
            Result.success(user)
        } catch(e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getInfoWithMe(): User? {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.me { user, error ->
                if(error != null) continuation.resume(null)
                else continuation.resume(user)
            }
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