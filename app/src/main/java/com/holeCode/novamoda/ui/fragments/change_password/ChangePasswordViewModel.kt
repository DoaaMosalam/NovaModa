package com.holeCode.novamoda.ui.fragments.change_password

import android.accounts.NetworkErrorException
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doaamosallam.domain.usecase.NovaUseCase
import com.doaamosallam.domain.usecase.ProfileUseCase
import com.holeCode.novamoda.common.lang
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val application: Application
):AndroidViewModel(application) {
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> get() = _errorMessage

    val currentPass = MutableLiveData<String>()
    val newPass = MutableLiveData<String>()

    fun changePass() {
        if (!currentPass.value.isNullOrEmpty() && !newPass.value.isNullOrEmpty()) {
            try {
                viewModelScope.launch {
                    val res = withContext(Dispatchers.IO) {
                        profileUseCase.changePass(
                            currentPass.value!!, newPass.value!!, lang
//                            ,authorization
                        )
                    }
                    if (res.status) {
                        Toast.makeText(getApplication(), res.message, Toast.LENGTH_SHORT).show()
                        currentPass.value = ""
                        newPass.value = ""
                    } else
                        Toast.makeText(getApplication(), res.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: NetworkErrorException) {
                Toast.makeText(getApplication(), "No internet connection", Toast.LENGTH_SHORT)
                    .show()
            }
        } else
            Toast.makeText(getApplication(), "Please Enter Password", Toast.LENGTH_SHORT).show()
    }

//    private val _errorMessage = MutableSharedFlow<String>()
//    val errorMessage: SharedFlow<String> get() = _errorMessage
//
//    private val _currentPass = MutableSharedFlow<String>(replay = 1)
//    val currentPass: SharedFlow<String> get() = _currentPass.asSharedFlow()
//
//    private val _newPass = MutableSharedFlow<String>(replay = 1)
//    val newPass: SharedFlow<String> get() = _newPass.asSharedFlow()
//
//    fun setCurrentPass(password: String) {
//        viewModelScope.launch {
//            _currentPass.emit(password)
//        }
//    }
//
//    fun setNewPass(password: String) {
//        viewModelScope.launch {
//            _newPass.emit(password)
//        }
//    }
//
//    fun changePass() {
//        viewModelScope.launch {
//            val currentPassValue = _currentPass.replayCache.firstOrNull()
//            val newPassValue = _newPass.replayCache.firstOrNull()
//
//            if (!currentPassValue.isNullOrEmpty() && !newPassValue.isNullOrEmpty()) {
//                try {
//                    val res = withContext(Dispatchers.IO) {
//                        novaUseCase.changePass(currentPassValue, newPassValue, lang)
//                    }
//                    if (res.status) {
//                        Toast.makeText(getApplication(), res.message, Toast.LENGTH_SHORT).show()
//                        _currentPass.emit("")
//                        _newPass.emit("")
//                    } else {
//                        Toast.makeText(getApplication(), res.message, Toast.LENGTH_SHORT).show()
//                    }
//                } catch (e: NetworkErrorException) {
//                    Toast.makeText(getApplication(), "No internet connection", Toast.LENGTH_SHORT).show()
//                } catch (e: Exception) {
//                    _errorMessage.emit("An error occurred: ${e.message}")
//                }
//            } else {
//                Toast.makeText(getApplication(), "Please Enter Password", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

}