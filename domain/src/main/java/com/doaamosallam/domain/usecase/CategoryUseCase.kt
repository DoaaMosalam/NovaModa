package com.doaamosallam.domain.usecase

import com.doaamosallam.domain.repo.CategoryRepo

class CategoryUseCase(private val categoryRepo: CategoryRepo) {
    suspend fun getCategoryData(lang: String) =
        categoryRepo.getCategoryData(lang)

    suspend fun getCategoryDetails(
        id: Int,
        lang: String
    ) = categoryRepo.getCategoryDetails(id, lang)

//    suspend fun addOrDeleteFavorite(
//        id: Int,
//        lang: String,
//
//    ) = categoryRepo.addOrDeleteFavorite(id, lang)

}