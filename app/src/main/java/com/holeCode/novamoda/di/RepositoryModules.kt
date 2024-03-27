package com.holeCode.novamoda.di

import com.holeCode.novamoda.data.repository.AuthRepository
import com.holeCode.novamoda.data.util.APIConsumer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModules {
    @Provides
    @Singleton
    fun provideAuthRepository(apiConsumer: APIConsumer): AuthRepository {
        return AuthRepository(apiConsumer)
    }
}