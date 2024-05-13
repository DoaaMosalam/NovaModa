package com.doaamosallam.domain.model.products

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    val id: Int,
    val price: Double,
    val old_price: Double,
    val discount: Double,
    val image: String,
    val name: String,
    val description: String,
    val images: List<String>,
    var in_favorites: Boolean,
    var in_cart: Boolean
):Parcelable