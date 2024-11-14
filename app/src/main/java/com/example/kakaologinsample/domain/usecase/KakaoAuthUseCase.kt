package com.example.kakaologinsample.domain.usecase

import com.example.kakaologinsample.domain.model.UserInfo
import com.example.kakaologinsample.data.repository.KakaoAuthRepository
import com.kakao.sdk.auth.model.OAuthToken
import javax.inject.Inject

class KakaoAuthUseCase @Inject constructor(
    private val kakaoAuthRepository: KakaoAuthRepository
){
    suspend fun login(): Result<OAuthToken?> {
        return kakaoAuthRepository.loginWithKakao()
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