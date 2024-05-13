package com.holeCode.novamoda.data.repository.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.holeCode.novamoda.data.repository.user.UserDataStoreRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val userPreference: UserDataStoreRepositoryImpl) : ViewModel() {
    suspend fun isUserLoggedIn() = userPreference.isUserLoggedIn()
    fun setIsLoggedIn(b: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreference.saveLoginState(b)
        }
    }
}

class UserViewModelFactory(private val userPreferencesRepository: UserDataStoreRepositoryImpl) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return UserViewModel(userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}