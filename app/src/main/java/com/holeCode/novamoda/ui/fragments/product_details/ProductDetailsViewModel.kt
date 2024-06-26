package com.holeCode.novamoda.ui.fragments.product_details

import android.accounts.NetworkErrorException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doaamosallam.domain.usecase.CartUseCase
import com.doaamosallam.domain.usecase.FavoritesUseCase
import com.holeCode.novamoda.common.lang
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val cartUseCase: CartUseCase,
    private val favoriteUseCase: FavoritesUseCase
) :ViewModel(){
    private val _errorMessage  = MutableSharedFlow<String>()
    val errorMessage:SharedFlow<String> get() = _errorMessage


    private val _favorite = MutableSharedFlow<Boolean>(replay = 1)
    val favorite: SharedFlow<Boolean> get() = _favorite.asSharedFlow()

    private val _addedToCart = MutableSharedFlow<Boolean>(replay = 1)
    val addedToCart: SharedFlow<Boolean> get() = _addedToCart.asSharedFlow()

fun addOrDeleteFavorite(id: Int) {
    viewModelScope.launch {
        iconFavoriteToggle()
        try {
            val res = withContext(Dispatchers.IO){
                favoriteUseCase.addOrDeleteFavorite(id, lang)
//                , authorization)
            }
            if (!res.status){
                iconFavoriteToggle()
            }
            _errorMessage.emit(res.message)
        } catch (e: NetworkErrorException) {
            _errorMessage.emit("No internet connection ${e.message}")
        }
    }
}
    fun addOrDeleteProductToCart(id: Int) {
        viewModelScope.launch {
            textCartToggle()
            try {
                val res = withContext(Dispatchers.IO) {
                    cartUseCase.addToCart(id, lang)
                    //                        , authorization)
                }
                if (!res.status) {
                    textCartToggle()
                }
                _errorMessage.emit(res.message.toString())
            } catch (e: NetworkErrorException) {
                _errorMessage.emit("No internet connection ${e.message}")
            }
        }
    }
    private suspend fun iconFavoriteToggle() {
        val currentValue = _favorite.replayCache.firstOrNull() ?: false
        _favorite.emit(!currentValue)
    }

    private suspend fun textCartToggle() {
        val currentValue = _addedToCart.replayCache.firstOrNull() ?: false
        _addedToCart.emit(!currentValue)
    }

    suspend fun favorite(inFavorite: Boolean) {
        _favorite.emit(inFavorite)
    }

    suspend fun textCart(inCart: Boolean) {
        _addedToCart.emit(inCart)
    }
}