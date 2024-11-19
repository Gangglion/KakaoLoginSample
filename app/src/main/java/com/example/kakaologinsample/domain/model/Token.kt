package com.example.kakaologinsample.domain.model

import com.kakao.sdk.auth.model.OAuthToken

data class Token(
    val accessToken: String,
    val refreshToken: String
)
internal fun OAuthToken.toData() = Token(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)