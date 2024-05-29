package com.doaamosallam.data.repo

import com.doaamosallam.data.data.APIService
import com.doaamosallam.domain.model.products.AddOrDeleteFavoriteModel
import com.doaamosallam.domain.model.products.CategoryDetailsModel
import com.doaamosallam.domain.model.products.CategoryModel
import com.doaamosallam.domain.repo.CategoryRepo

class CategoryRepoImp(private val apiService: APIService):CategoryRepo {
    override suspend fun getCategoryData(lang: String): CategoryModel =
        apiService.getCategoryData(lang)

    override suspend fun getCategoryDetails(id: Int, lang: String): CategoryDetailsModel =
        apiService.getCategoryDetails(id,lang)

//    override suspend fun addOrDeleteFavorite(
//        id: Int,
//        lang: String,
//    ): AddOrDeleteFavoriteModel = apiService.addOrDeleteFavorite(id, lang)

}