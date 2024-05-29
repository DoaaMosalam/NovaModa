package com.doaamosallam.domain.repo

import com.doaamosallam.domain.model.products.LogoutModel
import com.doaamosallam.domain.model.products.UserModel

interface ProfileRepo {

    suspend fun logOut(
        fcmToken: String,
        lang: String,
    ): LogoutModel

    suspend fun updateProfile(
        name: String,
        email: String,
        phone: String,
        lang: String,
    ): UserModel

    suspend fun changePass(
        currentPass: String,
        newPass: String,
        lang: String,
    ): LogoutModel
}