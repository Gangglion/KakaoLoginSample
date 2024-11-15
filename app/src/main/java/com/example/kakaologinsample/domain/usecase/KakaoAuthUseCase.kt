package com.example.kakaologinsample.domain.usecase

import android.content.Context
import com.example.kakaologinsample.domain.model.UserInfo
import com.example.kakaologinsample.data.repository.KakaoAuthRepository
import com.example.kakaologinsample.util.LogUtil
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KakaoAuthUseCase @Inject constructor(
    private val kakaoAuthRepository: KakaoAuthRepository
){
    suspend fun login(context: Context): Result<OAuthToken?> {
        return if(UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            loginWithKakaoTalk(context)
        } else {
            kakaoAuthRepository.kakaoAccountLogin()
        }
    }

    private suspend fun loginWithKakaoTalk(context: Context): Result<OAuthToken?> {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if(error != null) {
                    LogUtil.e("카카오톡을 통한 로그인 실패!!", error)
                    continuation.resume(Result.failure(error))
                }
                else continuation.resume(Result.success(token))
            }
        }
    }

    suspend fun getUserInfo(): Result<UserInfo?> {
        val user = kakaoAuthRepository.getUserInfo().getOrNull()
        if(user != null) {
            val userInfo = UserInfo(
                id = user.id,
                nickName = user.kakaoAccount?.profile?.nickname,
                profileImage = user.kakaoAccount?.profile?.profileImageUrl,
                email = user.kakaoAccount?.email
            )
            return Result.success(userInfo)
        } else {
            return Result.success(null)
        }
    }

    suspend fun logout(): Result<Boolean> {
        return kakaoAuthRepository.logout()
    }

    suspend fun unlink(): Result<Boolean> {
        return kakaoAuthRepository.unlink()
    }
}