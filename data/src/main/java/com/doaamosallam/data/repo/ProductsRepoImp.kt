package com.doaamosallam.data.repo

import com.doaamosallam.data.data.APIService
import com.doaamosallam.domain.model.products.AddOrDeleteFavoriteModel
import com.doaamosallam.domain.model.products.SearchModel
import com.doaamosallam.domain.model.request.HomeData
import com.doaamosallam.domain.repo.ProductsRepo

class ProductsRepoImp(private val apiService: APIService) : ProductsRepo {
    override suspend fun getAllProducts(lang: String): HomeData =
        apiService.getAllProducts(lang)

    override suspend fun addOrDeleteFavorite(id: Int, lang: String): AddOrDeleteFavoriteModel =
        apiService.addOrDeleteFavorite(id, lang)

    override suspend fun searchProducts(text: String, lang: String): SearchModel =
        apiService.searchProducts(text, lang)
}