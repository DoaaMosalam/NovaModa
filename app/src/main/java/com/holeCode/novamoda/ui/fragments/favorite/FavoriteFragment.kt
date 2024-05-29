package com.holeCode.novamoda.ui.fragments.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.holeCode.novamoda.R
import com.holeCode.novamoda.adapter.FavoriteAdapter
import com.holeCode.novamoda.common.BaseFragment
import com.holeCode.novamoda.databinding.FragmentFavoriteBinding
import com.holeCode.novamoda.util.OnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment :BaseFragment<FragmentFavoriteBinding,FavoriteViewModel>() {
    override fun getLayoutResID() = R.layout.fragment_favorite
    override val viewModel: FavoriteViewModel by viewModels()
    private lateinit var bottomNav: BottomNavigationView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitView()

    }

    override fun setUpInitView() {
        bottomNav = requireActivity().findViewById(R.id.bottomNavigation)


        val adapter = FavoriteAdapter(null, OnClickListener({
            null
        }, {
            viewModel.addOrDeleteFavorite(it)
        }, null, null))

        binding.recyclerFavorite.adapter = adapter
        lifecycleScope.launch {
            viewModel.products.collect { item ->
                adapter.setProducts(item)

            }
        }
    }


}