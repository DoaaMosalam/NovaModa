package com.holeCode.novamoda.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.holeCode.novamoda.repository.AuthRepository
import java.security.InvalidParameterException

class ResetActivityViewModelFactory(
    private val authRepository: AuthRepository,
    private val application: Application,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginActivityViewModel::class.java)) {
            return LoginActivityViewModel(authRepository, application) as T
        }
        throw InvalidParameterException("Unable to constructor Register Activity View Model")
    }


}