package com.doaamosallam.domain.repo

import com.doaamosallam.domain.model.products.AddOrDeleteFavoriteModel
import com.doaamosallam.domain.model.products.CartModel
import com.doaamosallam.domain.model.products.CartModelAddToCart
import com.doaamosallam.domain.model.products.CategoryDetailsModel
import com.doaamosallam.domain.model.products.CategoryModel
import com.doaamosallam.domain.model.products.EditQtyModel
import com.doaamosallam.domain.model.products.FavoritesModel
import com.doaamosallam.domain.model.products.LogoutModel
import com.doaamosallam.domain.model.products.RegisterModel
import com.doaamosallam.domain.model.products.SearchModel
import com.doaamosallam.domain.model.products.UserModel
import com.doaamosallam.domain.model.request.HomeData


interface NovaModaRepo {
    suspend fun login(email: String, password: String, lang: String): UserModel

    suspend fun register(model: RegisterModel, lang: String): UserModel

    suspend fun getAllHomeData(lang: String
//                               , authorization: String
    ): HomeData

    suspend fun addOrDeleteFavorite(
        id: Int,
        lang: String,
//        authorization: String
    ): AddOrDeleteFavoriteModel

    suspend fun searchProducts(
        text: String,
        lang: String,
//        authorization: String
    ): SearchModel

    suspend fun getCartData(
        lang: String
//        , authorization: String
    ): CartModel

    suspend fun addToCart(
        productId: Int,
        lang: String
//        ,authorization: String
    ): CartModelAddToCart

    suspend fun editQty(
        id: Int,
        qty: Int,
        lang: String,
//        authorization: String
    ): EditQtyModel

    suspend fun getCategoryData(lang: String): CategoryModel

    suspend fun getCategoryDetails(
        id: Int,
        lang: String,
//        authorization: String
    ): CategoryDetailsModel

    suspend fun logOut(
        fcmToken: String,
        lang: String,
//        authorization: String
    ): LogoutModel

    suspend fun updateProfile(
        name: String,
        email: String,
        phone: String,
        lang: String,
//        authorization: String
    ): UserModel

    suspend fun getFavorites(
        lang: String,
//        authorization: String
    ): FavoritesModel

    suspend fun changePass(
        currentPass: String,
        newPass: String,
        lang: String,
//        authorization: String
    ): LogoutModel
}