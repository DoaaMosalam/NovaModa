package com.holeCode.novamoda.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.holeCode.novamoda.fragments.BagFragment
import com.holeCode.novamoda.fragments.FavorityFragment
import com.holeCode.novamoda.fragments.HomeFragment
import com.holeCode.novamoda.fragments.ProfileFragment
import com.holeCode.novamoda.fragments.ShopFragment
class AdapterViewPager(fragmentAdapterViewPager: FragmentActivity):FragmentStateAdapter(fragmentAdapterViewPager) {
    private val fragmentList = listOf(
        HomeFragment(),
        ShopFragment(),
        BagFragment(),
        FavorityFragment(),
        ProfileFragment()
    )
    override fun getItemCount(): Int {
        return fragmentList.size }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position] }
}