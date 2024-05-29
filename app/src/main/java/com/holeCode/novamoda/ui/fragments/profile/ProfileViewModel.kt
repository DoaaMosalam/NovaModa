package com.holeCode.novamoda.ui.fragments.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doaamosallam.domain.model.products.UserData
import com.doaamosallam.domain.usecase.NovaUseCase
import com.doaamosallam.domain.usecase.ProfileUseCase
import com.holeCode.novamoda.common.lang
import com.holeCode.novamoda.common.saveUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val application: Application
):AndroidViewModel(application) {
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> get() = _errorMessage

    private val _changeUser = MutableLiveData(false)
    val changeUser: LiveData<Boolean> get() = _changeUser

    private val _showCancel = MutableLiveData(false)
    val showCancel: LiveData<Boolean> get() = _showCancel

    private val _showEdit = MutableLiveData(true)
    val showEdit: LiveData<Boolean> get() = _showEdit

    private val _showEdit2 = MutableLiveData(false)
    val showEdit2: LiveData<Boolean> get() = _showEdit2

    private val _showConsUp = MutableLiveData(true)
    val showConsUp: LiveData<Boolean> get() = _showConsUp

    private val _showConsDown = MutableLiveData(true)
    val showConsDown: LiveData<Boolean> get() = _showConsDown

    private val _user = MutableLiveData<UserData>()
    val user: LiveData<UserData> get() = _user

    private val _navigateToFavorite = MutableLiveData(false)
    val navigateToFavorite: LiveData<Boolean> get() = _navigateToFavorite

    private val _navigateToChangePass = MutableLiveData(false)
    val navigateToChangePass: LiveData<Boolean> get() = _navigateToChangePass

    val edtName = MutableLiveData<String>()


    val edtEmail = MutableLiveData<String>()

    val edtPhone = MutableLiveData<String>()


    fun setEdit2Show(showEdit: Boolean) {
        _showEdit2.value = showEdit
    }

    fun setData(user: UserData) {
        _user.value = user
        edtName.value = user.name!!
        edtEmail.value = user.email!!
        edtPhone.value = user.phone!!
    }

    fun onClickChange() {
        _showEdit.value = false
        _showCancel.value = true
        _showEdit2.value = false
        _showConsUp.value = true
        _showConsDown.value = false
    }

    fun onClickCancel() {
        _showEdit.value = true
        _showCancel.value = false
        _showEdit2.value = false
        _showConsUp.value = true
        _showConsDown.value = true
        setData(user.value!!)
    }

     fun onClickEdit2() = viewModelScope.launch {
        try {
            viewModelScope.launch {
                val res = withContext(Dispatchers.IO) {
                    profileUseCase.updateProfile(
                        edtName.value.toString(),
                        edtEmail.value.toString(),
                        edtPhone.value.toString(),
                        lang,
//                        authorization
                    )
                }
                if (res.status) {
                    _errorMessage.emit(res.message)
                    setData(res.data)

                    saveUser(
                        getApplication(),
                        res.data.name,
                        res.data.email,
                        res.data.phone,
                        res.data.token
                    )
                    _changeUser.value = true
                } else
                    _errorMessage.emit(res.message)
            }
        } catch (e: Exception) {

            _errorMessage.emit("No internet connection:${ e.message.toString()}")
        }
    }

    fun changeUserDone() {
        _changeUser.value = false
    }

    fun navigateToFavorite() {
        _navigateToFavorite.value = true
    }

    fun navigateToFavoriteDone() {
        _navigateToFavorite.value = false
    }

    fun navigateToChangePassword() {
        _navigateToChangePass.value = true
    }

    fun navigateToChangePassDone() {
        _navigateToChangePass.value = false
    }
}