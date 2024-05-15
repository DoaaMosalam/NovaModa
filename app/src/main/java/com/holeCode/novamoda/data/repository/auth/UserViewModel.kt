package com.holeCode.novamoda.data.repository.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.holeCode.novamoda.data.repository.user.UserDataStoreRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userPreference: UserDataStoreRepositoryImpl
) : ViewModel() {
    suspend fun isUserLoggedIn() = userPreference.isUserLoggedIn()
    fun setIsLoggedIn(b: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreference.saveLoginState(b)
        }
    }
}
