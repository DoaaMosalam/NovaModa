package com.holeCode.novamoda.data.repository.user

import com.holeCode.novamoda.data.local.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow

class UserDataStoreRepositoryImpl(
    private val userPreferencesDataSource: UserPreferencesDataSource
) : UserPreferenceRepository {
    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        userPreferencesDataSource.saveLoginState(isLoggedIn)
    }

    override suspend fun saveUserID(userId: String) {
        userPreferencesDataSource.saveUserID(userId)
    }

    override suspend fun isUserLoggedIn(): Flow<Boolean> {
        return userPreferencesDataSource.isUserLoggedIn
    }

    override fun getUserID(): Flow<String?> {
        return userPreferencesDataSource.userID
    }

}