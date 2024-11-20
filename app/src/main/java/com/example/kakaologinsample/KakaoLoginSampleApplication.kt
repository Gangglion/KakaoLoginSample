package com.example.kakaologinsample

import android.app.Application
import com.example.kakaologinsample.util.LogUtil
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KakaoLoginSampleApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.NATIVE_KEY)

        // 디버그 키 해시 가져오는 코드 - cmd에서 조회한 토큰값이 올바르지 않아 아래의 방법으로 토큰 발급하여 넣어줌.
        LogUtil.d(Utility.getKeyHash(applicationContext))
    }
}