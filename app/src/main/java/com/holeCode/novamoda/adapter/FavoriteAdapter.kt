package com.holeCode.novamoda.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.doaamosallam.domain.model.products.Data
import com.doaamosallam.domain.model.products.ProductModel
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.ItemHomeBinding
import com.holeCode.novamoda.util.OnClickListener

class FavoriteAdapter(
    private var products: List<Data>?,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    class ViewHolder(val itemHomeBinding: ItemHomeBinding) :
        RecyclerView.ViewHolder(itemHomeBinding.root) {
        fun bind(productModel: ProductModel) {
            //productModel.in_favorites = true
            itemHomeBinding.productModel = productModel
            itemHomeBinding.executePendingBindings()
        }

        fun iconFavoriteToggle(inFavorite: Boolean) {
            if (inFavorite)
                itemHomeBinding.imgButtonItemHome.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemHomeBinding.imgButtonItemHome.context,
                        R.drawable.ic_favorite_border
                    )
                )
            else
                itemHomeBinding.imgButtonItemHome.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemHomeBinding.imgButtonItemHome.context,
                        R.drawable.ic_favorite
                    )
                )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHomeBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        products!![position].product.in_favorites = true
//        holder.itemView.setOnClickListener {
//            onClickListener.onClick(products!![position].product)
//        }
        holder.itemHomeBinding.imgButtonItemHome.setOnClickListener {
            onClickListener.onClickButtonFavorite(products!![position].product.id)
            holder.iconFavoriteToggle(products!![position].product.in_favorites)
//            products!![position].product.in_favorites = !products!![position].product.in_favorites
//            notifyDataSetChanged()
        }
        holder.bind(products!![position].product)
    }

    override fun getItemCount(): Int {
        return products?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setProducts(products: List<Data>) {
        this.products = products
        notifyDataSetChanged()
    }
}