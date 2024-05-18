package com.holeCode.novamoda.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.holeCode.novamoda.R
import com.holeCode.novamoda.common.BaseFragment
import com.holeCode.novamoda.databinding.FragmentHomeBinding


class HomeFragment : BaseFragment<FragmentHomeBinding,HomeViewModel>() {
    override fun getLayoutResID()= R.layout.fragment_home
    override val viewModel: HomeViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater,getLayoutResID(),container,false)

        return binding.root
    }



    override fun setUpObserve() {

    }
}