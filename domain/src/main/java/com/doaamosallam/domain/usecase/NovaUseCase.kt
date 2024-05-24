package com.doaamosallam.domain.usecase

import com.doaamosallam.domain.model.products.RegisterModel
import com.doaamosallam.domain.repo.NovaModaRepo

class NovaUseCase(private val novaModaRepo: NovaModaRepo) {
    suspend fun login(email: String, password: String, lang: String) =
        novaModaRepo.login(email, password, lang)

    suspend fun register(model: RegisterModel, lang: String) = novaModaRepo.register(model, lang)

    suspend fun getAllHomeData(lang: String
//                            , authorization: String
    ) = novaModaRepo.getAllHomeData(lang)
//        , authorization)

    suspend fun addOrDeleteFavorite(
        id: Int,
        lang: String,
//        authorization: String
    ) = novaModaRepo.addOrDeleteFavorite(id, lang)
//    , authorization)

    suspend fun searchProducts(
        text: String,
        lang: String,
//        authorization: String
    ) = novaModaRepo.searchProducts(text, lang)
//    , authorization)

    suspend fun getCartData(
        lang: String,
//        authorization: String
    ) = novaModaRepo.getCartData(lang)
//        , authorization)

    suspend fun addToCart(
        productId: Int,
        lang: String
//        ,authorization: String
    ) = novaModaRepo.addToCart(productId, lang)
//        , authorization)

    suspend fun editQty(
        id: Int,
        qty: Int,
        lang: String,
//        authorization: String
    ) = novaModaRepo.editQty(id, qty, lang)
//    , authorization)

    suspend fun getCategoryData(lang: String) = novaModaRepo.getCategoryData(lang)

    suspend fun getCategoryDetails(
        id: Int,
        lang: String,
//        authorization: String
    ) = novaModaRepo.getCategoryDetails(id, lang)
//    , authorization)

    suspend fun logOut(
        fcmToken: String,
        lang: String,
//        authorization: String
    ) = novaModaRepo.logOut(fcmToken, lang)
//    , authorization)

    suspend fun updateProfile(
        name: String,
        email: String,
        phone: String,
        lang: String,
//        authorization: String
    ) = novaModaRepo.updateProfile(name, email, phone, lang)
//    , authorization)

    suspend fun getFavorites(
        lang: String,
//        authorization: String
    ) = novaModaRepo.getFavorites(lang)
//    , authorization)

    suspend fun changePass(
        currentPass: String,
        newPass: String,
        lang: String,
//        authorization: String
    ) = novaModaRepo.changePass(currentPass, newPass, lang)
//    , authorization)

}