package com.holeCode.novamoda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.holeCode.novamoda.adapter.AdapterViewPager
import com.holeCode.novamoda.databinding.ActivityMainScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainScreenActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainScreenBinding
    private lateinit var mNavController: NavController
    private lateinit var adapterFragment:AdapterViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
        //==========================================================================
        //add nav controller
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavController = navHostFragment.navController

        adapterFragment = AdapterViewPager(this)
        bindingMain.viewPagerMain.adapter = adapterFragment
        // handle View pager about bottom navigation view
        CoroutineScope(Dispatchers.Main).launch {
            bindingMain.viewPagerMain.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    bindingMain.bottomNavigationView.menu.getItem(position).isChecked = true

                }
            })

            bindingMain.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.home -> bindingMain.viewPagerMain.currentItem = 0
                    R.id.shop -> bindingMain.viewPagerMain.currentItem = 1
                    R.id.bag -> bindingMain.viewPagerMain.currentItem = 2
                    R.id.favorite -> bindingMain.viewPagerMain.currentItem = 3
                    R.id.profile -> bindingMain.viewPagerMain.currentItem = 4

                }
                true
            }
        }
    }
}