package com.example.kakaologinsample.data.datastore.di

import com.example.kakaologinsample.data.datastore.repository.DataStoreRepository
import com.example.kakaologinsample.data.datastore.repository.DataStoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreModule {
    @Singleton
    @Binds
    fun bindsDataStoreRepository(
        dataStoreRepositoryImpl: DataStoreRepositoryImpl
    ) : DataStoreRepository
}