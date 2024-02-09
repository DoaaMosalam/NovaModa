package com.holeCode.novamoda.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holeCode.novamoda.pojo.LoginBody
import com.holeCode.novamoda.pojo.User
import com.holeCode.novamoda.repository.AuthRepository
import com.holeCode.novamoda.storage.Firebasedb
import com.holeCode.novamoda.storage.SharedPreferencesManager
import com.holeCode.novamoda.util.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class LoginActivityViewModel  (
    private val authRepository: AuthRepository,
    val application: Application,
) : ViewModel() {
    private var isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    private var user: MutableLiveData<User> = MutableLiveData()
    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage
    fun getUser(): LiveData<User> = user
    private var firebasedb: Firebasedb
    init {
        firebasedb = Firebasedb()
    }

    fun loginUserVM(body: LoginBody) {
        viewModelScope.launch {
            authRepository.loginUser(body).collect {
                when (it) {
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }

                    is RequestStatus.Success -> {
                        isLoading.value = false
                        user.value = it.data.data as User?
                        SharedPreferencesManager.getInstance(application.baseContext)
                            .loadUserLogin(body)
                    }

                    is RequestStatus.Error -> {
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }
    }

    fun loginUserByFirebase(email: String, password: String) {
        viewModelScope.launch {
            firebasedb.loginUserByFirebase(email, password)

        }
    }
}