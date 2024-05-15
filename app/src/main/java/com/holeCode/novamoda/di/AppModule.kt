package com.holeCode.novamoda.di

import android.app.Application
import android.content.Context
import com.holeCode.novamoda.data.repository.auth.FirebaseAuthRepository
import com.holeCode.novamoda.data.repository.auth.FirebaseAuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFirebaseAuthRepository(): FirebaseAuthRepository {
        return FirebaseAuthRepositoryImpl() // Assuming FirebaseAuthRepositoryImpl is your implementation
    }
    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}