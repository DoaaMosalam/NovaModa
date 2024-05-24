package com.holeCode.novamoda.common

import android.accounts.NetworkErrorException
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doaamosallam.domain.model.products.CartModel
import com.doaamosallam.domain.model.products.ProductModel
import com.doaamosallam.domain.model.products.UserData
import com.doaamosallam.domain.usecase.NovaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeActivityViewModel @Inject constructor(
    private val novaUseCase: NovaUseCase,
    application: Application
) : AndroidViewModel(application) {


    private val _user = MutableLiveData<UserData>()
    val user: LiveData<UserData> get() = _user

    private val _text = MutableLiveData("")
    val text: LiveData<String> get() = _text

    private val _products = MutableLiveData<List<ProductModel>>()
    val products: LiveData<List<ProductModel>> get() = _products

    private val _cart = MutableLiveData<CartModel>()
    val cart: LiveData<CartModel> get() = _cart

    private val _badge = MutableLiveData<Int>()
    val badge: LiveData<Int> get() = _badge

    private val _logOut = MutableLiveData(false)
    val logOut: LiveData<Boolean> get() = _logOut

    private val _language = MutableLiveData("en") // Default to English if `lang` is not defined
    val language: LiveData<String> get() = _language

    init {
        getCartData()
    }

    fun setText(text: String) {
        _text.value = text
    }

    fun addOrDeleteFavorite(id: Int) {
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    novaUseCase.addOrDeleteFavorite(id, _language.value ?: "en")
//                    , _user.value?.token ?: "")
                }
                Toast.makeText(getApplication(), res.message, Toast.LENGTH_SHORT).show()
                setText(_text.value ?: "")
            } catch (e: NetworkErrorException) {
                Toast.makeText(getApplication(), "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getCartData() {
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    novaUseCase.getCartData(_language.value ?: "en")
//                        , _user.value?.token ?: "")
                }
                if (res.status) {
                    _cart.value = res
                } else {
                    Toast.makeText(getApplication(), res.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: NetworkErrorException) {
                Toast.makeText(getApplication(), "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setBadge(badgeNumber: Int) {
        _badge.value = badgeNumber
    }

    fun setUser(userData: UserData) {
        _user.value = userData
    }

    fun logOut() {
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    novaUseCase.logOut("SomeFcmToken", _language.value ?: "en")
//                        , _user.value?.token ?: "")
                }
                if (res.status) {
                    _logOut.value = true
                } else {
                    Toast.makeText(getApplication(), res.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: NetworkErrorException) {
                Toast.makeText(getApplication(), "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setLang(lang: String) {
        _language.value = lang
    }
}