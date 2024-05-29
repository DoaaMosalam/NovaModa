package com.doaamosallam.data.repo

import com.doaamosallam.data.data.APIService
import com.doaamosallam.domain.model.products.CartModel
import com.doaamosallam.domain.model.products.CartModelAddToCart
import com.doaamosallam.domain.model.products.EditQtyModel
import com.doaamosallam.domain.repo.CartRepo

class CartRepoImp(private val apiService: APIService):CartRepo {
    override suspend fun getCartData(lang: String): CartModel =
        apiService.getCartData(lang)

    override suspend fun addToCart(productId: Int, lang: String): CartModelAddToCart =
        apiService.addToCart(productId, lang)
    override suspend fun editQty(id: Int, qty: Int, lang: String): EditQtyModel =
        apiService.editQty(id, qty, lang)
}