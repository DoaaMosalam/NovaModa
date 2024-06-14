package com.holeCode.novamoda.di

import com.doaamosallam.data.data.APIService
import com.doaamosallam.data.repo.AuthNovaRepoImp
import com.doaamosallam.data.repo.CartRepoImp
import com.doaamosallam.data.repo.CategoryRepoImp
import com.doaamosallam.data.repo.FavoritesRepoImp
import com.doaamosallam.data.repo.ProductsRepoImp
import com.doaamosallam.data.repo.ProfileRepoImp
import com.doaamosallam.domain.repo.AuthNovaRepo
import com.doaamosallam.domain.repo.CartRepo
import com.doaamosallam.domain.repo.CategoryRepo
import com.doaamosallam.domain.repo.FavoritesRepo
import com.doaamosallam.domain.repo.ProductsRepo
import com.doaamosallam.domain.repo.ProfileRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun provideAuthRepo(apiService: APIService):AuthNovaRepo{
        return AuthNovaRepoImp(apiService)
    }
    @Provides
    fun provideProductsRepo(apiService: APIService):ProductsRepo{
        return ProductsRepoImp(apiService)
    }

    @Provides
    fun ProvideCategoryRepo(apiService: APIService):CategoryRepo{
        return CategoryRepoImp(apiService)
    }

    @Provides
    fun ProvidesFavoritesRepo(apiService: APIService):FavoritesRepo{
        return FavoritesRepoImp(apiService)
    }
    @Provides
    fun ProvideCartRepo(apiService: APIService):CartRepo{
        return CartRepoImp(apiService)
    }

    @Provides
    fun ProvidesProfileRepo(apiService: APIService):ProfileRepo{
        return ProfileRepoImp(apiService)
    }

}