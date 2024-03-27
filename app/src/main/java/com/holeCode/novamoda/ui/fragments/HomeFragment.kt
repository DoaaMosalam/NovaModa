package com.holeCode.novamoda.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.holeCode.novamoda.BasicFragment
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.FragmentHomeBinding
import com.holeCode.novamoda.domain.adapter.HomeViewPagerAdapter
import com.holeCode.novamoda.ui.fragments.categories.LightingFragment
import com.holeCode.novamoda.ui.fragments.categories.ClothesFragment
import com.holeCode.novamoda.ui.fragments.categories.PreventFragment
import com.holeCode.novamoda.ui.fragments.categories.ElectronicFragment
import com.holeCode.novamoda.ui.fragments.categories.MainCategoryFragment
import com.holeCode.novamoda.ui.fragments.categories.SportsFragment

class HomeFragment : BasicFragment<FragmentHomeBinding>() {
//    private lateinit var homeBinding:FragmentHomeBinding
override fun getLayoutResID()= R.layout.fragment_home
    private lateinit var homeViewPagerAdapter: HomeViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
//        return homeBinding.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ClothesFragment(),
            PreventFragment(),
            ElectronicFragment(),
            SportsFragment(),
            LightingFragment()
        )
       homeViewPagerAdapter = HomeViewPagerAdapter(categoriesFragment,childFragmentManager,lifecycle)
        binding.viewpagerHome.adapter = homeViewPagerAdapter
        TabLayoutMediator(binding.tabLayout,binding.viewpagerHome){tab,position->
            when(position){
                0->tab.text = "All"
                1->tab.text = "electronic"
                2->tab.text = "prevent"
                3->tab.text = "sports"
                4->tab.text = "lighting"
                5->tab.text = "clothes"
            }
        }.attach()
    }
}