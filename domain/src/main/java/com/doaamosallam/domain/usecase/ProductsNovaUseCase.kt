package com.doaamosallam.domain.usecase

import com.doaamosallam.domain.repo.ProductsRepo

class ProductsNovaUseCase(private val productsRepo: ProductsRepo) {
    suspend fun getAllProducts(
        lan: String

    ) = productsRepo.getAllProducts(lan)

//    suspend fun addOrDeleteFavorite(
//        id: Int, lang: String
//    ) = productsRepo.addOrDeleteFavorite(id, lang)

    suspend fun searchProducts(
        text: String,
        lang: String,
    ) = productsRepo.searchProducts(text, lang)
}