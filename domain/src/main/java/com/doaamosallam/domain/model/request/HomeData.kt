package com.doaamosallam.domain.model.request

import com.doaamosallam.domain.model.products.BannerModel
import com.doaamosallam.domain.model.products.ProductModel

data class HomeData(
    val `data`: DataObject,
    val status: Boolean
)
data class DataObject(
    val banners:List<BannerModel>,
    val products:List<ProductModel>,
    val ad:String
)
