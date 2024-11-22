package com.example.kakaologinsample.data.kakao.di

import com.example.kakaologinsample.data.kakao.repository.KakaoRepository
import com.example.kakaologinsample.data.kakao.repository.KakaoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class KakaoModule {
    @Binds
    abstract fun bindsKakaoRepository(
        kakaoRepositoryImpl: KakaoRepositoryImpl
    ): KakaoRepository
}