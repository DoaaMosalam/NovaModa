package com.holeCode.novamoda.di

import com.holeCode.novamoda.data.repository.auth.FirebaseAuthRepository
import com.holeCode.novamoda.data.repository.auth.FirebaseAuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideFirebaseAuthRepository(): FirebaseAuthRepository {
        return FirebaseAuthRepositoryImpl() // Assuming FirebaseAuthRepositoryImpl is your implementation
    }
}