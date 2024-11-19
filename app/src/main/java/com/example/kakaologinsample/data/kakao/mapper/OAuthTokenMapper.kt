package com.example.kakaologinsample.data.kakao.mapper

import com.example.kakaologinsample.data.kakao.model.Token
import com.kakao.sdk.auth.model.OAuthToken

fun OAuthToken.toData() = Token(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)