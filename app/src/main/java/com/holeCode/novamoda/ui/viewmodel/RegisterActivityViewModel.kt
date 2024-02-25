package com.holeCode.novamoda.ui.viewmodel


import android.app.Application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holeCode.novamoda.data.request.ValidateEmailBody
import com.holeCode.novamoda.domain.model.RegisterBody
import com.holeCode.novamoda.domain.model.User
import com.holeCode.novamoda.data.repository.AuthRepository
import com.holeCode.novamoda.data.remote.Firebasedb
import com.holeCode.novamoda.data.local.SharedPreferencesManager
import com.holeCode.novamoda.data.util.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterActivityViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    val application: Application
) : ViewModel() {
    private var isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    private var isUniqueEmail: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private var user: MutableLiveData<User> = MutableLiveData()
    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage
    fun getIsUniqueEmail(): LiveData<Boolean> = isUniqueEmail
    fun getUser(): LiveData<User> = user

    private var firebaseAuthenticationManager: Firebasedb

    init {
        firebaseAuthenticationManager = Firebasedb()
    }

    fun validateEmailAddress(body: ValidateEmailBody) {
        viewModelScope.launch {
            authRepository.validateEmailAddress(body).collect {
                when (it) {
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }

                    is RequestStatus.Success -> {
                        isLoading.value = false
                        isUniqueEmail.value = it.data.isUnique
                    }

                    is RequestStatus.Error -> {
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }
    }

    fun registerUserVM(body: RegisterBody) {
        viewModelScope.launch {
            authRepository.registerUser(body).collect {
                when (it) {
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }

                    is RequestStatus.Success -> {
                        isLoading.value = false
                        user.value = it.data.data as User?
                        //save token using shared preference
//                        SharedPreferencesManager.getInstance(application.baseContext).token
                        SharedPreferencesManager.getInstance(application.baseContext).saveUser(body)
                    }

                    is RequestStatus.Error -> {
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }
    }

    fun registerUserByFirebase(email: String, password: String) {
        viewModelScope.launch {
            firebaseAuthenticationManager.registerUserByFirebase(email, password)
        }
    }
}