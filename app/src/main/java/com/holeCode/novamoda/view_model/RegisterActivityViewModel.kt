package com.holeCode.novamoda.view_model


import android.app.Application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holeCode.novamoda.auth.data.ValidateEmailBody
import com.holeCode.novamoda.pojo.RegisterBody
import com.holeCode.novamoda.pojo.User
import com.holeCode.novamoda.repository.AuthRepository
import com.holeCode.novamoda.storage.Firebasedb
import com.holeCode.novamoda.storage.SharedPreferencesManager
import com.holeCode.novamoda.util.RequestStatus
import kotlinx.coroutines.launch

class RegisterActivityViewModel(
    private val authRepository: AuthRepository, val application: Application
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

//    fun uploadDataFirebase(uid:String,name:String,phone:String,email:String,password:String){
//        viewModelScope.launch { firebaseAuthenticationManager.uploadData(uid,name,phone,email,password) }
//    }
}