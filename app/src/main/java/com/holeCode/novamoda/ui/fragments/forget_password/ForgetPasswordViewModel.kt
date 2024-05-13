package com.holeCode.novamoda.ui.fragments.forget_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holeCode.novamoda.data.model.Resource
import com.holeCode.novamoda.data.repository.auth.FirebaseAuthRepository
import com.holeCode.novamoda.util.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    val authRepository: FirebaseAuthRepository
):ViewModel() {
    private val _forgetPasswordState = MutableSharedFlow<Resource<String>>()
    val forgetPasswordState: SharedFlow<Resource<String>> = _forgetPasswordState.asSharedFlow()

    val email = MutableStateFlow("")
    fun sendUpdatePasswordEmail() = viewModelScope.launch (IO) {
        if (email.value.isEmailValid()){
            authRepository.sendUpdatePasswordEmail(email.value).collect(){
                _forgetPasswordState.emit(it)
            }
        }else{
            _forgetPasswordState.emit(Resource.Error(Exception("Invalid email")))
        }

    }
}