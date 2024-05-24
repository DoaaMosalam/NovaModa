package com.doaamosallam.data.repo

import com.doaamosallam.data.data.APIService
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
import com.doaamosallam.domain.repo.NovaModaRepo

class NovaModaRepoImp(private val apiService: APIService):NovaModaRepo {
    override suspend fun login(email: String, password: String, lang: String):UserModel =
        apiService.login(email, password, lang)

    override suspend fun register(model: RegisterModel, lang: String): UserModel =
        apiService.register(model, lang)

    override suspend fun getAllHomeData(lang: String
//                                        , authorization: String
    ): HomeData =
        apiService.getAllHomeData(lang)
//    , authorization)


    override suspend fun addOrDeleteFavorite(
        id: Int,
        lang: String,
//        authorization: String
    ): AddOrDeleteFavoriteModel = apiService.addOrDeleteFavorite(id, lang)
//    , authorization)

    override suspend fun searchProducts(
        text: String,
        lang: String,
//        authorization: String
    ): SearchModel = apiService.searchProducts(text, lang)
//    , authorization)

    override suspend fun getCartData(
        lang: String
//                                     , authorization: String
    ): CartModel =
        apiService.getCartData(lang)
//    , authorization)

    override suspend fun addToCart(
        productId: Int,
        lang: String,
//        authorization: String
    ): CartModelAddToCart = apiService.addToCart(productId, lang)
//        , authorization)

    override suspend fun editQty(
        id: Int,
        qty: Int,
        lang: String,
//        authorization: String
    ): EditQtyModel =
        apiService.editQty(id, qty, lang)
//    , authorization)

    override suspend fun getCategoryData(lang: String): CategoryModel =
        apiService.getCategoryData(lang)

    override suspend fun getCategoryDetails(
        id: Int,
        lang: String,
//        authorization: String
    ): CategoryDetailsModel = apiService.getCategoryDetails(id, lang)
//    , authorization)

    override suspend fun logOut(
        fcmToken: String,
        lang: String,
//        authorization: String
    ): LogoutModel =
        apiService.logOut(fcmToken, lang)
//    , authorization)

    override suspend fun updateProfile(
        name: String,
        email: String,
        phone: String,
        lang: String,
//        authorization: String
    ): UserModel = apiService.updateProfile(name, email, phone, lang)
//    , authorization)

    override suspend fun getFavorites(
        lang: String,
//                                      authorization: String
    ): FavoritesModel =
        apiService.getFavorites(lang)
//    , authorization)

    override suspend fun changePass(
        currentPass: String,
        newPass: String,
        lang: String,
//        authorization: String
    ): LogoutModel = apiService.changePass(currentPass, newPass, lang)
//    , authorization)

}