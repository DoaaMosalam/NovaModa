package com.holeCode.novamoda.ui.fragments.bag

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.holeCode.novamoda.R
import com.holeCode.novamoda.adapter.CartAdapter
import com.holeCode.novamoda.common.BaseFragment
import com.holeCode.novamoda.common.HomeActivityViewModel
import com.holeCode.novamoda.databinding.FragmentBagBinding
import com.holeCode.novamoda.util.OnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BagFragment : BaseFragment<FragmentBagBinding,BagViewModel>(){
    override fun getLayoutResID() = R.layout.fragment_bag
    override val viewModel: BagViewModel by viewModels()
    private val sharedViewModel by viewModels<HomeActivityViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCartData()
        setUpInitView()
    }

    override fun setUpInitView() {
        lifecycleScope.launch {
            viewModel.errorMessage.collect { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }

        val cartAdapter = CartAdapter(OnClickListener({
            // handle item click
        }, {
            viewModel.addOrDeleteFavorite(it)
        }, {
            viewModel.addOrDeleteProductToCart(it)
        }, { id, qty ->
            viewModel.editQty(id, qty)
        }))

        binding.recyclerCart.adapter = cartAdapter

        lifecycleScope.launch {
            viewModel.cartData.collect { cartItems ->
                cartAdapter.setData(cartItems)
                sharedViewModel.setBadge(cartItems.size)
            }
        }

    }

}