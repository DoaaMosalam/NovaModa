package com.holeCode.novamoda.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.doaamosallam.domain.model.products.BannerModel
import com.holeCode.novamoda.databinding.ItemSliderBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(private val bannerList: List<BannerModel>) :
    SliderViewAdapter<SliderAdapter.ViewHolder>() {
    class ViewHolder(private val itemSliderBinding: ItemSliderBinding) :
        SliderViewAdapter.ViewHolder(itemSliderBinding.root) {
        fun bind(bannerData: BannerModel) {
            itemSliderBinding.bannerData = bannerData.image
            itemSliderBinding.executePendingBindings()
        }
    }

    override fun getCount(): Int {
        return bannerList.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        return ViewHolder(ItemSliderBinding.inflate(LayoutInflater.from(parent!!.context)))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        viewHolder!!.bind(bannerList[position])
    }
}