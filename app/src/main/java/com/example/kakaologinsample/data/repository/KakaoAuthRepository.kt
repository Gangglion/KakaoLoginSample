package com.example.kakaologinsample.data.repository

import android.content.Context
import com.kakao.sdk.auth.AuthApiClient
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
    suspend fun loginWithKakao() : Result<OAuthToken?> {
        return try {
            val token = loginWithKakaoTalk() ?: loginWithKakaoAccount()
            Result.success(token)

        } catch(e : Exception) {
            Result.failure(e)
        }
    }

    private suspend fun loginWithKakaoTalk(): OAuthToken? {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if(error != null) continuation.resume(null)
                else continuation.resume(token)
            }
        }
    }

    private suspend fun loginWithKakaoAccount(): OAuthToken? {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if(error != null) continuation.resume(null)
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