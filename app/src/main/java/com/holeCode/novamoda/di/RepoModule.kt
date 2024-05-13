package com.holeCode.novamoda.di

import com.doaamosallam.data.data.APIService
import com.doaamosallam.data.repo.NovaModaRepoImp
import com.doaamosallam.domain.repo.NovaModaRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    fun provideRepo(apiService: APIService): NovaModaRepo {
        return NovaModaRepoImp(apiService)

    }
}