package com.holeCode.novamoda.ui.fragments.favorite

import android.accounts.NetworkErrorException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doaamosallam.domain.model.products.Data
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
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCase: FavoritesUseCase
) :ViewModel() {
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> get() = _errorMessage

    private val _products = MutableSharedFlow<List<Data>>()
    val products: SharedFlow<List<Data>> get() = _products

    init {
        getFavoriteData()
    }

    private fun getFavoriteData() {
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    favoriteUseCase.getFavorites(lang)
//                    , authorization)
                }
                if (res.status) {
                    _products.emit(res.data.data)
                } else {
                    _errorMessage.emit(res.message)                }
            } catch (e: NetworkErrorException) {
                _errorMessage.emit("No internet connection:${ e.message.toString()}")
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
                    _errorMessage.emit(res.message)
                }
                getFavoriteData()
            } catch (e: NetworkErrorException) {
                _errorMessage.emit("No internet connection:${ e.message.toString()}")
            }
        }
    }
}