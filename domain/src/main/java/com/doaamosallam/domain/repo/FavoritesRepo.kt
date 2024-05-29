package com.doaamosallam.domain.repo

import com.doaamosallam.domain.model.products.AddOrDeleteFavoriteModel
import com.doaamosallam.domain.model.products.FavoritesModel

interface FavoritesRepo {
    suspend fun addOrDeleteFavorite(
        id: Int,
        lang: String,
    ): AddOrDeleteFavoriteModel

    suspend fun getFavorites(
        lang: String
    ):FavoritesModel
}