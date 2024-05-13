package com.holeCode.novamoda.data.repository.auth

import com.holeCode.novamoda.data.model.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    suspend fun loginWithGoogle(
        idToken:String
    ): Flow<Resource<String>>

    suspend fun loginWithFacebook(
        token: String
    ):Flow<Resource<String>>
    suspend fun sendUpdatePasswordEmail(email: String): Flow<Resource<String>>

    fun logout()
}