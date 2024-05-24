package com.holeCode.novamoda.ui.fragments.product_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.holeCode.novamoda.R
import com.holeCode.novamoda.adapter.SliderProductAdapter
import com.holeCode.novamoda.common.BaseFragment
import com.holeCode.novamoda.databinding.FragmentProductDetailsBinding
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding,ProductDetailsViewModel>() {
    override fun getLayoutResID() = R.layout.fragment_product_details
    override val viewModel: ProductDetailsViewModel by viewModels()
    private val navArgs by navArgs<ProductDetailsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
       _binding = DataBindingUtil.inflate(inflater,getLayoutResID(),container,false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpObserve()


    }
    override fun setUpObserve() {
        val productModel = navArgs.product
       lifecycleScope.launch {
            viewModel.favorite(productModel.in_favorites)
            viewModel.textCart(productModel.in_cart)
        }


       lifecycleScope.launch {
           viewModel.errorMessage.collect{error ->
               error.let {
                   Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
               }
           }
       }

        binding.sliderProductDetails.setIndicatorAnimation(IndicatorAnimationType.WORM)
        binding.sliderProductDetails.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
        binding.sliderProductDetails.setSliderAdapter(SliderProductAdapter(navArgs.product.images))

        binding.btnAddToCart.setOnClickListener {
            viewModel.addOrDeleteProductToCart(productModel.id)
        }
        binding.imgButtonProductDetails.setOnClickListener {
            viewModel.addOrDeleteFavorite(productModel.id)
        }

       lifecycleScope.launch {
            viewModel.favorite.collect { isFavorite ->
                productModel.in_favorites = isFavorite
            binding.productModel = productModel
            }
        }

        lifecycleScope.launch {
            viewModel.addedToCart.collect { isInCart ->
                productModel.in_cart = isInCart
            binding.productModel = productModel
            }
        }
//        viewModel.addedToCart.observe(viewLifecycleOwner) {
//            productModel.in_cart = it
//            binding.productModel = productModel
////            sharedViewModel.getCartData()
//        }
//        viewModel.favorite.observe(viewLifecycleOwner) {
//            productModel.in_favorites = it
//            binding.productModel = productModel
//        }


    }

}