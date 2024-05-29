package com.doaamosallam.domain.repo

import com.doaamosallam.domain.model.products.AddOrDeleteFavoriteModel
import com.doaamosallam.domain.model.products.CategoryDetailsModel
import com.doaamosallam.domain.model.products.CategoryModel

interface CategoryRepo {
    suspend fun getCategoryData(lang:String):CategoryModel

    suspend fun getCategoryDetails(
        id:Int,
        lang: String
    ):CategoryDetailsModel

//    suspend fun addOrDeleteFavorite(
//        id:Int,
//        lang:String,
//    ): AddOrDeleteFavoriteModel

}