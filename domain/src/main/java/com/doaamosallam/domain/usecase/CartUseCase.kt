package com.doaamosallam.domain.usecase

import com.doaamosallam.domain.repo.CartRepo

class CartUseCase(private val cartRepo: CartRepo) {
    suspend fun getCartData(
        lang: String,

    ) = cartRepo.getCartData(lang)

    suspend fun addToCart(
        productId: Int,
        lang: String
    ) = cartRepo.addToCart(productId, lang)

    suspend fun editQty(
        id: Int,
        qty: Int,
        lang: String,
    ) = cartRepo.editQty(id, qty, lang)

}