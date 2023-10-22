package com.holeCode.novamoda.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holeCode.novamoda.auth.UpdatePasswordActivity
import com.holeCode.novamoda.pojo.ResetPasswordBody
import com.holeCode.novamoda.pojo.User
import com.holeCode.novamoda.repository.AuthRepository
import com.holeCode.novamoda.storage.FirebaseAuthenticationManager
import com.holeCode.novamoda.util.RequestStatus
import kotlinx.coroutines.launch

class ResetActivityViewModel(
    private val authRepository: AuthRepository,
    val application: Application
) :ViewModel() {
    private var isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    private var user: MutableLiveData<User> = MutableLiveData()
    private var firebaseAuthenticationManager: FirebaseAuthenticationManager
    init {
        firebaseAuthenticationManager = FirebaseAuthenticationManager()
    }

    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage
    fun getUser(): LiveData<User> = user

    fun resetPasswordVM(body: ResetPasswordBody) {
        viewModelScope.launch {
            authRepository.resetPassword(body).collect {
                when (it) {
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }

                    is RequestStatus.Success -> {
                        isLoading.value = false
                        user.value = it.data.data as User?
                    }

                    is RequestStatus.Error -> {
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }
    }
    fun resetPasswordByFirebase(email: String) {
        viewModelScope.launch {
            firebaseAuthenticationManager.resetPasswordByFirebase(email)

        }
    }

}