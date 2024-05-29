package com.holeCode.novamoda.ui.fragments.shop.products

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.holeCode.novamoda.R
import com.holeCode.novamoda.adapter.HomeAdapter
import com.holeCode.novamoda.common.BaseFragment
import com.holeCode.novamoda.databinding.FragmentCategoryProductsBinding
import com.holeCode.novamoda.ui.fragments.shop.ShopViewModel
import com.holeCode.novamoda.util.OnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CategoryProducts : BaseFragment<FragmentCategoryProductsBinding,ShopViewModel>() {
    override val viewModel: ShopViewModel by viewModels()
    override fun getLayoutResID() =R.layout.fragment_category_products
    private val navArgs by navArgs<CategoryProductsArgs>()
    private lateinit var bottomNav: BottomNavigationView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitView()
        fetchCategoryDetails()
    }
    private fun fetchCategoryDetails() {
        viewModel.getCategoryDetails(navArgs.categoryId)
    }
    override fun setUpInitView() {
        bottomNav = requireActivity().findViewById(R.id.bottomNavigation)


        val adapter = HomeAdapter(OnClickListener({
            findNavController().navigate(
                CategoryProductsDirections.actionCategoryProductsToProductDetailsFragment(
                    it
                )
            )
        }, {
            viewModel.addOrDeleteFavorite(it, navArgs.categoryId)
        }, null, null))
        binding.recyclerCategoryDetails.adapter = adapter

        lifecycleScope.launch {
            viewModel.categoryDetails.collect { response ->
                response.data.let {
                    adapter.submitList(it.data)
                }
            }
        }
    }

}