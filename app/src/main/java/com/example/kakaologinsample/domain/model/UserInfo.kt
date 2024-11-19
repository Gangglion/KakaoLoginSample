package com.example.kakaologinsample.domain.model

import com.kakao.sdk.user.model.User

data class UserInfo(
    val id: Long? = null,
    val nickName: String? = null,
    val profileImage: String? = null,
    val email: String? = null
)
internal fun User.toData() = UserInfo(
    id = this.id,
    nickName = this.kakaoAccount?.profile?.nickname,
    profileImage = this.kakaoAccount?.profile?.profileImageUrl,
    email = this.kakaoAccount?.email
)