package com.holeCode.novamoda.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.doaamosallam.domain.model.products.ProductModel
import com.holeCode.novamoda.databinding.ItemHomeBinding
import com.holeCode.novamoda.util.OnClickListener

class HomeAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<ProductModel, HomeAdapter.ViewHolder>(HomeDiffUtil()) {
    class ViewHolder(val itemHomeBinding: ItemHomeBinding) :
        RecyclerView.ViewHolder(itemHomeBinding.root) {
        fun bind(productModel: ProductModel) {
            itemHomeBinding.productModel = productModel
            itemHomeBinding.executePendingBindings()
            Log.d("bindNumbers", "yesss")
        }

        fun iconFavoriteToggle(productModel: ProductModel) {
            productModel.in_favorites = !productModel.in_favorites
            bind(productModel)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHomeBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        Log.d("bindNumbers", "yes")
        holder.itemView.setOnClickListener {
            onClickListener.onClick(getItem(position))
        }
        holder.itemHomeBinding.imgButtonItemHome.setOnClickListener {
            onClickListener.onClickButtonFavorite(getItem(position).id)
            holder.iconFavoriteToggle(getItem(position))
        }
    }

    class HomeDiffUtil : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem == newItem
        }

    }
}