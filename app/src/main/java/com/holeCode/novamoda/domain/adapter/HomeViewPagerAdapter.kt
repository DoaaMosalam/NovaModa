package com.holeCode.novamoda.domain.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.lifecycle.Lifecycle



class HomeViewPagerAdapter(
    private val fragments:List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

//    fragmentAdapterViewPager: FragmentActivity) :
//    FragmentStateAdapter(fragmentAdapterViewPager) {
//    private val fragmentList = listOf(
//        ChairFragment(),
//        CuppoardFragment(),
//        FurrnitryFragment(),
//        TableFragment(),
//        AccessoryFragment()
//    )
//
//    override fun getItemCount(): Int {
//        return fragmentList.size
//    }
//
//    override fun createFragment(position: Int): Fragment {
//        return fragmentList[position]
//    }
}