package com.doaamosallam.domain.repo

import com.doaamosallam.domain.model.products.AddOrDeleteFavoriteModel
import com.doaamosallam.domain.model.products.SearchModel
import com.doaamosallam.domain.model.request.HomeData

interface ProductsRepo {

    suspend fun getAllProducts(lang:String
    ):HomeData

    suspend fun addOrDeleteFavorite(
        id:Int,
        lang:String,
    ): AddOrDeleteFavoriteModel

    suspend fun searchProducts(
        text: String,
        lang: String,
//        authorization: String
    ): SearchModel
}