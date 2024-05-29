package com.doaamosallam.domain.repo

import com.doaamosallam.domain.model.products.RegisterModel
import com.doaamosallam.domain.model.products.UserModel

interface AuthNovaRepo {
    suspend fun login(email: String, password: String, lang: String): UserModel

    suspend fun register(model: RegisterModel, lang: String): UserModel

}