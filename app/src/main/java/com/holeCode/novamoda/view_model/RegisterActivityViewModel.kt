package com.holeCode.novamoda.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.holeCode.novamoda.data.ValidateEmailBody
import com.holeCode.novamoda.pojo.User
import com.holeCode.novamoda.repository.AuthRepository

class RegisterActivityViewModel(val authRepository: AuthRepository, val application: Application) :
    ViewModel() {
        private var isLoading:MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {value = false}
    private var errorMessage:MutableLiveData<HashMap<String,String>> = MutableLiveData()
    private var isUnique:MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    private var user:MutableLiveData<User> = MutableLiveData()

    fun getIsLoading():LiveData<Boolean> = isLoading
    fun getErrorMessage():LiveData<HashMap<String,String>> = errorMessage
    fun getIsUnique():LiveData<Boolean> = isUnique
    fun isUser():LiveData<User> =user

}