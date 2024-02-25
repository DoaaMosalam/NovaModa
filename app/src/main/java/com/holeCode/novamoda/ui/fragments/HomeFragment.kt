package com.holeCode.novamoda.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.FragmentHomeBinding
import com.holeCode.novamoda.domain.adapter.HomeViewPagerAdapter
import com.holeCode.novamoda.ui.fragments.categories.AccessoryFragment
import com.holeCode.novamoda.ui.fragments.categories.ChairFragment
import com.holeCode.novamoda.ui.fragments.categories.CupboardFragment
import com.holeCode.novamoda.ui.fragments.categories.FurnitureFragment
import com.holeCode.novamoda.ui.fragments.categories.MainCategoryFragment
import com.holeCode.novamoda.ui.fragments.categories.TableFragment

class HomeFragment : Fragment() {
    private lateinit var homeBinding:FragmentHomeBinding
    private lateinit var homeViewPagerAdapter: HomeViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            FurnitureFragment(),
            TableFragment(),
            AccessoryFragment()
        )
       homeViewPagerAdapter = HomeViewPagerAdapter(categoriesFragment,childFragmentManager,lifecycle)
        homeBinding.viewpagerHome.adapter = homeViewPagerAdapter
        TabLayoutMediator(homeBinding.tabLayout,homeBinding.viewpagerHome){tab,position->
            when(position){
                0->tab.text = "All"
                1->tab.text = "Chair"
                2->tab.text = "Cupboard"
                3->tab.text = "Furniture"
                4->tab.text = "Table"
                5->tab.text = "Accessory"
            }
        }.attach()


    }

}