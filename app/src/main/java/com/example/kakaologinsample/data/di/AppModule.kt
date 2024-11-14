package com.example.kakaologinsample.data.di

import android.content.Context
import androidx.room.Room
import com.example.kakaologinsample.data.local.dao.UserTokenDao
import com.example.kakaologinsample.data.local.database.UserTokenDatabase
import com.example.kakaologinsample.data.repository.KakaoAuthRepository
import com.example.kakaologinsample.data.repository.UserTokenDbRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

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
     * hilt 를 사용하여 db 객체 생성 후 dao 주입
     */
    @Provides
    fun provideRoomDbInstance(@ApplicationContext context: Context) : UserTokenDao {
        return Room.databaseBuilder(
            context,
            UserTokenDatabase::class.java, "user-token-db"
        ).build().userTokenDao()
    }

    /**
     * hilt 를 사용하여 UserTokenDbRepository 주입
     */
    @Provides
    fun provideUserTokenDbRepository(userTokenDao: UserTokenDao) : UserTokenDbRepository {
        return UserTokenDbRepository(userTokenDao)
    }
}