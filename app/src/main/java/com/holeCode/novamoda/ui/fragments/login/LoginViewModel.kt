package com.holeCode.novamoda.ui.fragments.login

import android.app.Application
import android.util.Log

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doaamosallam.domain.model.products.UserData
import com.doaamosallam.domain.usecase.AuthNovaUseCase
import com.holeCode.novamoda.data.model.Resource
import com.holeCode.novamoda.data.repository.auth.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val  authNovaUseCase: AuthNovaUseCase,
    private val authRepository: FirebaseAuthRepository,
   private val  application: Application
) : ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _user = MutableSharedFlow<UserData>()
    val user: SharedFlow<UserData> get() = _user

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _navigateToRegister = MutableLiveData(false)
    val navigateToRegister: LiveData<Boolean> get() = _navigateToRegister

    private val _navigateToForgetPassword = MutableLiveData(false)
    val navigateToForgetPassword get() = _navigateToForgetPassword

    private val _navigateToHome = MutableLiveData(false)
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    private val _loginState = MutableSharedFlow<Resource<String>>()
    val loginState: SharedFlow<Resource<String>> = _loginState.asSharedFlow()

    fun login() = viewModelScope.launch {
        val email = email.value.toString()
        val password = password.value.toString()
        try {
            val result = withContext(Dispatchers.IO) {
                authNovaUseCase.login(email, password, "en")
            }
            if (result.status) {
                _user.emit(result.data)
                _navigateToHome.value = true
                Log.d("Login", "Authentication successful for: $email $password")

            }else {
                Log.d("Login", "Authentication failed for: $email $password")
                _errorMessage.value = "Login failed: ${result.message}"
            }

        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "An error occurred"
            Toast.makeText(application, _errorMessage.value, Toast.LENGTH_SHORT).show()
        }
    }

    fun loginWithGoogle(idToken:String) = viewModelScope.launch {
        authRepository.loginWithGoogle(idToken).onEach {resource->
            when(resource){
                is Resource.Success->{
                    _loginState.emit(Resource.Success(resource.data?:"Empty User Id"))
                }

                else -> _loginState.emit(resource)
            }
        }.launchIn(viewModelScope)
    }

    fun loginWithFacebook(token: String) =viewModelScope.launch{
        authRepository.loginWithFacebook(token).onEach { resource ->
            when(resource){
                is Resource.Success->
                    _loginState.emit(Resource.Success(resource.data?:"Empty User Id"))

                else -> _loginState.emit(resource)
            }
        }.launchIn(viewModelScope)

    }

    fun navigateToRegister() {
        _navigateToRegister.value = true
    }

    fun navigateToRegisterDone() {
        _navigateToRegister.value = false
    }

    fun navigateToHomeDone() {
        _navigateToHome.value = false
    }
    fun navigateToForgetPassword(){
        _navigateToForgetPassword.value=true
    }
    fun navigateToForgetPasswordDone() {
        _navigateToForgetPassword.value = false
    }

}