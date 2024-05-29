package com.holeCode.novamoda.ui.fragments.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doaamosallam.domain.model.products.RegisterModel
import com.doaamosallam.domain.usecase.AuthNovaUseCase
import com.doaamosallam.domain.usecase.NovaUseCase
import com.holeCode.novamoda.TransactionManager
import com.holeCode.novamoda.common.lang
import com.holeCode.novamoda.data.local.SharedPreferencesManager
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
    private val authNovaUseCase: AuthNovaUseCase,
    private val application: Application
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

    private val transactionManager = TransactionManager()

    init {
        transactionManager.registerCallbackForPendingTransactions()
    }

    fun register()= viewModelScope.launch {

        try {
            val result = withContext(Dispatchers.IO){
                authNovaUseCase.register(
                    RegisterModel(
                        name.value.toString(),
                        email.value.toString(),
                        password.value.toString(),
                        phone.value.toString()
                    ), lang
                )
            }
            if (result.status){
                Log.d("register", "Authentication Register Successful for:$name $phone $email $password")
                // Save user data to SharedPreferences
                SharedPreferencesManager.getInstance(application.baseContext)
                    .saveUser(application, name.value, email.value, phone.value, password.value)

                // Clear input fields
                email.value = ""
                name.value = ""
                password.value = ""
                phone.value = ""

                // Notify transaction complete
                transactionManager.completeTransaction()
            }
            _errorMessage.value = "Register failed: ${result.message}"

        }catch (e:Exception){
            _errorMessage.value = e.message ?: "An error occurred"
        }

    }

}
