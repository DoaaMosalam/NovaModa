package com.doaamosallam.data.repo

import com.doaamosallam.data.data.APIService
import com.doaamosallam.domain.model.products.LogoutModel
import com.doaamosallam.domain.model.products.UserModel
import com.doaamosallam.domain.repo.ProfileRepo

class ProfileRepoImp(private val apiService:APIService):ProfileRepo {
    override suspend fun logOut(
        fcmToken: String,
        lang: String,
    ): LogoutModel =
        apiService.logOut(fcmToken, lang)

    override suspend fun updateProfile(
        name: String,
        email: String,
        phone: String,
        lang: String,
    ): UserModel = apiService.updateProfile(name, email, phone, lang)


    override suspend fun changePass(
        currentPass: String,
        newPass: String,
        lang: String,
    ): LogoutModel = apiService.changePass(currentPass, newPass, lang)
}