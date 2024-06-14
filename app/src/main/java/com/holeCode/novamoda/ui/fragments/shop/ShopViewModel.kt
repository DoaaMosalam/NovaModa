package com.holeCode.novamoda.ui.fragments.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doaamosallam.domain.model.products.CategoryDetailsModel
import com.doaamosallam.domain.model.products.CategoryModel
import com.doaamosallam.domain.usecase.CategoryUseCase
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
class ShopViewModel @Inject constructor(
    private val categoryUseCase: CategoryUseCase,
    private val favoriteUseCase: FavoritesUseCase
):ViewModel(){
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage:SharedFlow<String> get() = _errorMessage.asSharedFlow()

    private val _categoriesItems = MutableSharedFlow<CategoryModel>()
    val categoriesItems: SharedFlow<CategoryModel> get() = _categoriesItems

    private val _categoryDetails = MutableSharedFlow<CategoryDetailsModel>()
    val categoryDetails: SharedFlow<CategoryDetailsModel> get() = _categoryDetails

    init {
        getCategoryData()
    }

    private fun getCategoryData() {
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    categoryUseCase.getCategoryData(lang)
                }
                if (res.status) {
                    _categoriesItems.emit(res)
                }
                    _errorMessage.emit(res.message.toString())
            } catch (e: Exception) {
                _errorMessage.emit("No internet connection ${e.message.toString()}")

            }
        }
    }

    fun getCategoryDetails(id: Int) {
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    categoryUseCase.getCategoryDetails(id, lang)
//                    , authorization)
                }
                if (res.status) {
                    _categoryDetails.emit(res)
                }
                    _errorMessage.emit(res.message.toString())

            } catch (e: Exception) {
                _errorMessage.emit("No internet connection ${e.message.toString()}")
            }
        }
    }

    fun addOrDeleteFavorite(id: Int, categoryId: Int) {
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    favoriteUseCase.addOrDeleteFavorite(id, lang)
//                    , authorization)
                }
                if (res.status) {
                    _errorMessage.emit(res.message)
                } else {
                    getCategoryDetails(categoryId)
                    _errorMessage.emit(res.message.toString())
                }
            } catch (e: Exception) {
                _errorMessage.emit("No internet connection ${e.message.toString()}")

            }
        }
    }
}