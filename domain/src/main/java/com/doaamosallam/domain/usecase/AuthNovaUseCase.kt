package com.doaamosallam.domain.usecase

import com.doaamosallam.domain.model.products.RegisterModel
import com.doaamosallam.domain.repo.AuthNovaRepo

class AuthNovaUseCase(private val authRepo: AuthNovaRepo) {
    suspend fun login(email: String, password: String, lang: String) =
        authRepo.login(email, password, lang)

    suspend fun register(model: RegisterModel, lang: String) =
        authRepo.register(model, lang)

}