package com.holeCode.novamoda.di

import com.doaamosallam.domain.repo.NovaModaRepo
import com.doaamosallam.domain.usecase.NovaUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideUseCase(novaModaRepo: NovaModaRepo): NovaUseCase {
        return NovaUseCase(novaModaRepo)
    }
}