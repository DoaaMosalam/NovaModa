package com.doaamosallam.domain.usecase

import com.doaamosallam.domain.repo.ProfileRepo

class ProfileUseCase(private val profileRepo: ProfileRepo) {
    suspend fun logOut(
        fcmToken: String,
        lang: String,
    ) = profileRepo.logOut(fcmToken, lang)

    suspend fun updateProfile(
        name: String,
        email: String,
        phone: String,
        lang: String,
    ) = profileRepo.updateProfile(name, email, phone, lang)

    suspend fun changePass(
        currentPass: String,
        newPass: String,
        lang: String,
    ) = profileRepo.changePass(currentPass, newPass, lang)

}