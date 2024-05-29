package com.holeCode.novamoda.di

import com.doaamosallam.domain.repo.AuthNovaRepo
import com.doaamosallam.domain.repo.CartRepo
import com.doaamosallam.domain.repo.CategoryRepo
import com.doaamosallam.domain.repo.FavoritesRepo
import com.doaamosallam.domain.repo.NovaModaRepo
import com.doaamosallam.domain.repo.ProductsRepo
import com.doaamosallam.domain.repo.ProfileRepo
import com.doaamosallam.domain.usecase.AuthNovaUseCase
import com.doaamosallam.domain.usecase.CartUseCase
import com.doaamosallam.domain.usecase.CategoryUseCase
import com.doaamosallam.domain.usecase.FavoritesUseCase
import com.doaamosallam.domain.usecase.NovaUseCase
import com.doaamosallam.domain.usecase.ProductsNovaUseCase
import com.doaamosallam.domain.usecase.ProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {


    @Provides
    fun ProvideAuthUseCase(authNovaRepo: AuthNovaRepo):AuthNovaUseCase{
        return AuthNovaUseCase(authNovaRepo)
    }
    @Provides
    fun ProvideProductsUseCase(productsNovaRepo: ProductsRepo):ProductsNovaUseCase{
        return ProductsNovaUseCase(productsNovaRepo)
    }
    @Provides
    fun ProvideCategoryUseCase(categoryRepo: CategoryRepo):CategoryUseCase{
        return CategoryUseCase(categoryRepo)
    }
    @Provides
    fun ProvidesFavoritesUseCase(favoritesRepo: FavoritesRepo):FavoritesUseCase{
        return FavoritesUseCase(favoritesRepo)
    }
    @Provides
    fun ProvideCartUseCase(cartRepo: CartRepo):CartUseCase{
        return CartUseCase(cartRepo)
    }

    @Provides
    fun ProvideProfileUSeCase(profileRepo: ProfileRepo):ProfileUseCase{
        return ProfileUseCase(profileRepo)
    }
//    @Provides
//    fun provideUseCase(novaModaRepo: NovaModaRepo): NovaUseCase {
//        return NovaUseCase(novaModaRepo)
//    }
}
