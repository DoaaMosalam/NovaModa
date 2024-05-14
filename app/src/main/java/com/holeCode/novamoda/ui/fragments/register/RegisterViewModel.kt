package com.holeCode.novamoda.ui.fragments.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doaamosallam.domain.model.products.RegisterModel
import com.doaamosallam.domain.usecase.NovaUseCase
import com.holeCode.novamoda.common.lang
import com.holeCode.novamoda.data.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val novaUseCase: NovaUseCase
) : ViewModel() {
    val name = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val email= MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val  _registerUser = MutableLiveData<RegisterModel>()
    val registerUser :LiveData<RegisterModel> get() = _registerUser

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage :LiveData<String> get() = _errorMessage


    private val _registerState = MutableSharedFlow<Resource<String>>()
    val registerState: SharedFlow<Resource<String>> = _registerState.asSharedFlow()

    fun register()= viewModelScope.launch {
        val name = name.value.toString()
        val phone = phone.value.toString()
        val email = email.value.toString()
        val password = password.value.toString()

        val registerModel = RegisterModel(name, phone, email, password)

        try {
            val result = withContext(Dispatchers.IO){ novaUseCase.register(registerModel, lang)}
            if (result.status){
                Log.d("register", "Authentication Register Successful for:$name $phone $email $password")
                _registerUser.value =  registerModel
            }else{
                _errorMessage.value = "Register failed: ${result.message}"
            }

        }catch (e:Exception){
            _errorMessage.value = e.message ?: "An error occurred"
        }

    }

}
