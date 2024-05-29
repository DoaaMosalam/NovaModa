package com.doaamosallam.data.repo

import com.doaamosallam.data.data.APIService
import com.doaamosallam.domain.model.products.RegisterModel
import com.doaamosallam.domain.model.products.UserModel
import com.doaamosallam.domain.repo.AuthNovaRepo

class AuthNovaRepoImp(private val apiService: APIService):AuthNovaRepo {
    override suspend fun login(email: String, password: String, lang: String): UserModel =
        apiService.login(email, password, lang)


    override suspend fun register(model: RegisterModel, lang: String): UserModel =
        apiService.register(model, lang)

}