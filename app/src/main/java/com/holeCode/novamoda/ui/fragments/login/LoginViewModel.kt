package com.holeCode.novamoda.ui.fragments.login

import android.app.Application
import android.util.Log

import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doaamosallam.domain.model.products.UserData
import com.doaamosallam.domain.usecase.NovaUseCase
import com.holeCode.novamoda.data.model.Resource
import com.holeCode.novamoda.data.repository.auth.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
    private val novaUseCase: NovaUseCase,
    private val authRepository: FirebaseAuthRepository,
    application: Application
) : AndroidViewModel(application) {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _user = MutableLiveData<UserData>()
    val user: LiveData<UserData> get() = _user

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _loginState = MutableSharedFlow<Resource<String>>()
    val loginState: SharedFlow<Resource<String>> = _loginState.asSharedFlow()

    fun login() = viewModelScope.launch {
        val email = email.value.toString()
        val password = password.value.toString()

        if (email.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Email or password cannot be empty"
            return@launch
        }
        try {
            val result = withContext(Dispatchers.IO) {
                novaUseCase.login(email, password, "en")
            }
            if (result.status) {
                _user.value = result.data
                Log.d("Login", "Authentication sucess for: $email $password")


            } else {
                Log.d("Login", "Authentication failed for: $email $password")
                _errorMessage.value = "Login failed: ${result.message}"

            }
        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "An error occurred"
            Toast.makeText(getApplication(), _errorMessage.value, Toast.LENGTH_SHORT).show()
        }
    }

//    private fun handleLoginFlow(loginFlow: suspend () -> Flow<Resource<UserDetailsModel>>) =
//        viewModelScope.launch(IO) {
//            loginFlow().collect { resource ->
//                when (resource) {
//                    is Resource.Success -> {
//                        savePreferenceData(resource.data!!)
//                        _loginState.emit(Resource.Success(resource.data))
//                    }
//
//                    else -> _loginState.emit(resource)
//                }
//            }
//        }

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

}