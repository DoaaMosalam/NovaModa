package com.doaamosallam.domain.repo

import com.doaamosallam.domain.model.products.CartModel
import com.doaamosallam.domain.model.products.CartModelAddToCart
import com.doaamosallam.domain.model.products.EditQtyModel

interface CartRepo {
    suspend fun getCartData(
        lang: String
    ): CartModel

    suspend fun addToCart(
        productId: Int,
        lang: String
    ): CartModelAddToCart

    suspend fun editQty(
        id: Int,
        qty: Int,
        lang: String,
    ): EditQtyModel
}