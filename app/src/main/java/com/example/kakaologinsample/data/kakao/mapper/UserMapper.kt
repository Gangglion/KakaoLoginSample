package com.example.kakaologinsample.data.kakao.mapper

import com.example.kakaologinsample.data.kakao.model.UserInfo
import com.kakao.sdk.user.model.User

fun User.toData() = UserInfo(
    id = this.id,
    nickName = this.kakaoAccount?.profile?.nickname,
    profileImage = this.kakaoAccount?.profile?.profileImageUrl,
    email = this.kakaoAccount?.email
)