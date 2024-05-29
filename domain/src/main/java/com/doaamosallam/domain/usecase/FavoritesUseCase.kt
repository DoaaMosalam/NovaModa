package com.doaamosallam.domain.usecase


import com.doaamosallam.domain.repo.FavoritesRepo

class FavoritesUseCase(private val favoriteRepo: FavoritesRepo) {

    suspend fun addOrDeleteFavorite(
        id: Int,
        lang: String,
    ) = favoriteRepo.addOrDeleteFavorite(id, lang)

    suspend fun getFavorites(
        lang: String,
    ) = favoriteRepo.getFavorites(lang)
//    , authorization)
}