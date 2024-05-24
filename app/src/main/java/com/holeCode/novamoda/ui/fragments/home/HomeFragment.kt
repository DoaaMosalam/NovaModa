package com.holeCode.novamoda.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.holeCode.novamoda.R
import com.holeCode.novamoda.adapter.HomeAdapter
import com.holeCode.novamoda.adapter.SliderAdapter
import com.holeCode.novamoda.common.BaseFragment
import com.holeCode.novamoda.databinding.FragmentHomeBinding
import com.holeCode.novamoda.util.OnClickListener
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun getLayoutResID() = R.layout.fragment_home
    override val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getHomeData()

        setUpObserve()
        setUpSearch()

    }


    override fun setUpObserve() {

        lifecycleScope.launch {
            viewModel.errorMessage.collect {error->
                error.let {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.bannerList.collect { banner ->
                val bannerAdapter = SliderAdapter(banner)
                binding.sliderViewImage.setSliderAdapter(bannerAdapter)
            binding.sliderViewImage.setIndicatorAnimation(IndicatorAnimationType.WORM)
            binding.sliderViewImage.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
            binding.sliderViewImage.startAutoCycle()
            }
        }

       val  productAdapter = HomeAdapter(
            OnClickListener({
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(
                        it
                    )
                )
            }, {
                viewModel.addOrDeleteFavorite(it)
            }, null, null)
        )
        binding.recyclerHome.adapter = productAdapter
        lifecycleScope.launch {
            viewModel.productList.collect { products ->
                productAdapter.submitList(products)
            }
        }

    }

    private fun setUpSearch() {
        binding.edtSearch.setOnEditorActionListener { _, _, _ ->
            val searchText = binding.edtSearch.text.toString()
            if (searchText.isNotEmpty()) {
                lifecycleScope.launch {
                    viewModel.search(searchText)
                }
            }
            true
        }
    }

}

