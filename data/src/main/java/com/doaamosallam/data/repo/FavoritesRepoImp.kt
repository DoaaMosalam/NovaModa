package com.doaamosallam.data.repo

import com.doaamosallam.data.data.APIService
import com.doaamosallam.domain.model.products.AddOrDeleteFavoriteModel
import com.doaamosallam.domain.model.products.FavoritesModel
import com.doaamosallam.domain.repo.FavoritesRepo

class FavoritesRepoImp(private val apiService: APIService): FavoritesRepo {
    override suspend fun addOrDeleteFavorite(id: Int, lang: String): AddOrDeleteFavoriteModel =
        apiService.addOrDeleteFavorite(id, lang)

    override suspend fun getFavorites(lang: String): FavoritesModel =
        apiService.getFavorites(lang)
}