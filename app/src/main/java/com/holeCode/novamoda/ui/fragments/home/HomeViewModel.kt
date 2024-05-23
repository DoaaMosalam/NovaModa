package com.holeCode.novamoda.ui.fragments.home

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doaamosallam.domain.model.products.BannerModel
import com.doaamosallam.domain.model.products.ProductModel
import com.doaamosallam.domain.usecase.NovaUseCase
import com.holeCode.novamoda.common.lang
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val novaUseCase: NovaUseCase,
) : ViewModel() {
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> get() = _errorMessage

    private val _bannerList = MutableSharedFlow<List<BannerModel>>()
    val bannerList: SharedFlow<List<BannerModel>> get() = _bannerList

    private val _productList = MutableSharedFlow<List<ProductModel>>()
    val productList: SharedFlow<List<ProductModel>> get() = _productList


    init {
        getHomeData()
    }

    fun addOrDeleteFavorite(id: Int) {
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    novaUseCase.addOrDeleteFavorite(id, lang)
//                        , authorization)
                }
                if (res.status) {
                    _errorMessage.emit(res.message)

                } else {
                    getHomeData()
                    _errorMessage.emit(res.message)
                }
            } catch (e: NetworkErrorException) {
                _errorMessage.emit("No internet connection:${ e.message.toString()}")
            }
        }
    }

    fun getHomeData() {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.Main) {
                    novaUseCase.getAllHomeData(lang)
                }
                _bannerList.emit(data.data.banners)
                _productList.emit(data.data.products)
                Log.d("home", "Home is: $_productList")
            } catch (e: NetworkErrorException) {
                _errorMessage.emit(e.message.toString())
            }
        }
    }

    suspend fun search(text: String) {
        if (text.isNotEmpty()) {
            try {
                viewModelScope.launch {
                    val result = withContext(Dispatchers.IO) {
                        novaUseCase.searchProducts(text, lang)
//                        , authorization)
                    }
                    _productList.emit(result.data.data)
                }
            } catch (e: NetworkErrorException) {

                _errorMessage.emit(e.message.toString())
            }
        }
    }


}