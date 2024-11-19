package com.example.kakaologinsample.domain.usecase

import android.content.Context
import com.example.kakaologinsample.data.repository.kakao.repository.KakaoRepository
import com.example.kakaologinsample.domain.model.Token
import com.example.kakaologinsample.domain.model.UserInfo
import com.kakao.sdk.user.UserApiClient
import javax.inject.Inject

class KakaoAuthUseCase @Inject constructor(
    private val kakaoRepository: KakaoRepository
){
    suspend fun login(activityContext: Context): Token? {
        return if (UserApiClient.instance.isKakaoTalkLoginAvailable(activityContext)) {
            val resultKakaoTalk = kakaoRepository.kakaoTalkLogin(activityContext)
            resultKakaoTalk.fold(
                onSuccess = { data ->
                    data
                },
                onFailure = { _ ->
                    null
                }
            )
        } else {
            val resultKakaoAccount = kakaoRepository.loginWithKakaoAccount()
            resultKakaoAccount.fold(
                onSuccess = { data ->
                    data
                },
                onFailure = { _ ->
                    null
                }
            )
        }
    }

    suspend fun getUserInfo(): UserInfo? {
        val result = kakaoRepository.getUserInfo()
        return result.fold(
            onSuccess = { data ->
                data
            },
            onFailure = { _ ->
                null
            }
        )
    }

    suspend fun logout(): Result<Boolean> {
        return kakaoRepository.logout()
    }

    suspend fun unlink(): Result<Boolean> {
        return kakaoRepository.unlink()
    }
}