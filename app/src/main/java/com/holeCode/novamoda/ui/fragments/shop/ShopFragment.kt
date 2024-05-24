package com.holeCode.novamoda.ui.fragments.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.holeCode.novamoda.R
import com.holeCode.novamoda.adapter.CategoriesAdapter
import com.holeCode.novamoda.adapter.OnClickListenerCategory
import com.holeCode.novamoda.common.BaseFragment
import com.holeCode.novamoda.databinding.FragmentShopBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShopFragment : BaseFragment<FragmentShopBinding,ShopViewModel>() {
    override fun getLayoutResID()= R.layout.fragment_shop
    override val viewModel: ShopViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserve()
    }

    override fun setUpObserve() {

        lifecycleScope.launch {
            viewModel.errorMessage.collect{error->
                error.let {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        val adapter = CategoriesAdapter(OnClickListenerCategory {
            viewModel.getCategoryDetails(it)
            findNavController().navigate(
                ShopFragmentDirections.actionShopFragmentToCategoryProducts(
                    it
                )
            )
        })

        lifecycleScope.launch{
            viewModel.categoriesItems.collect{
                adapter.setData(it.data.data)
                binding.recyclerCategory.adapter = adapter
            }
        }

    }

}