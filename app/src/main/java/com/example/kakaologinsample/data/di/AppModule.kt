package com.example.kakaologinsample.data.di

import android.content.Context
import com.example.kakaologinsample.data.repository.KakaoAuthRepository
import com.example.kakaologinsample.data.repository.UserPrefDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * hilt 를 사용하여 KakaoAuthRepository 주입
     */
    @Provides
    fun provideKakaoAuthRepository(@ApplicationContext context: Context): KakaoAuthRepository {
        return KakaoAuthRepository(context)
    }

    /**
     * hilt 를 사용하여 UserPrefDataStore 주입
     */
    @Provides
    fun provideUserPrefDataStore(@ApplicationContext context: Context): UserPrefDataStore {
        return UserPrefDataStore(context)
    }
}