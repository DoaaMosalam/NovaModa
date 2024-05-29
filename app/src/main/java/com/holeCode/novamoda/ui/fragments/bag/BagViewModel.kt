package com.holeCode.novamoda.ui.fragments.bag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doaamosallam.domain.model.products.CartItems
import com.doaamosallam.domain.usecase.CartUseCase
import com.doaamosallam.domain.usecase.FavoritesUseCase
import com.holeCode.novamoda.common.lang
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BagViewModel @Inject constructor(
    private val cartUseCase: CartUseCase,
    private val favoriteUseCase: FavoritesUseCase
):ViewModel() {
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> get() = _errorMessage

    private val _cartData = MutableSharedFlow<List<CartItems>>()
    val cartData: SharedFlow<List<CartItems>> get() = _cartData

    init {
        getCartData()
    }


    fun getCartData() {
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    cartUseCase.getCartData(lang)
//                    , authorization)
                }
                if (res.status) {
                    _cartData.emit(res.data.cart_items)
                } else {
                    _errorMessage.emit(res.message.toString())
                }
            } catch (e: Exception) {
                _errorMessage.emit("No internet connection${_errorMessage.emit(e.message.toString())}")

            }

        }
    }

    fun addOrDeleteFavorite(id: Int) {
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    favoriteUseCase.addOrDeleteFavorite(id, lang)
//                    , authorization)
                }
                if (res.status) {
                    _errorMessage.emit(res.message)
                } else {
                    getCartData()
                    _errorMessage.emit(res.message)
                }
            } catch (e: Exception) {
                _errorMessage.emit("No internet connection${_errorMessage.emit(e.message.toString())}")
            }
        }
    }

    fun addOrDeleteProductToCart(id: Int) {
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    cartUseCase.addToCart(id, lang)
//                    , authorization)
                }
                if (res.status) {
                    _errorMessage.emit(res.message.toString())
                    getCartData()
                } else {
                    _errorMessage.emit(res.message.toString())
                }
            } catch (e: Exception) {
                _errorMessage.emit("No internet connection${_errorMessage.emit(e.message.toString())}")

            }
        }
    }

    fun editQty(id: Int, qty: Int) {
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    cartUseCase.editQty(id, qty, lang)
//                    , authorization)
                }
                if (res.status) {
                    _errorMessage.emit(res.message.toString())
                    getCartData()
                } else {
                    _errorMessage.emit(res.message.toString())
                }
            } catch (e: Exception) {
                _errorMessage.emit("No internet connection${_errorMessage.emit(e.message.toString())}")
            }
        }
    }
}