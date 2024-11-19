package com.example.kakaologinsample.data.repository.kakao.di

import com.example.kakaologinsample.data.repository.kakao.repository.KakaoRepository
import com.example.kakaologinsample.data.repository.kakao.repository.KakaoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface KakaoModule {
    @Singleton
    @Binds
    fun bindsKakaoRepository(
        kakaoRepositoryImpl: KakaoRepositoryImpl
    ): KakaoRepository
}