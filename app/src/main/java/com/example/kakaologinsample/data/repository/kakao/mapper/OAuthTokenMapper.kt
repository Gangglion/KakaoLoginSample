package com.example.kakaologinsample.data.repository.kakao.mapper

import com.example.kakaologinsample.data.repository.kakao.model.Token
import com.kakao.sdk.auth.model.OAuthToken

fun OAuthToken.toData() = Token(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)