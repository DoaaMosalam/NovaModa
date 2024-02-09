package com.holeCode.novamoda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.common.util.DataUtils
import com.holeCode.novamoda.adapter.AdapterViewPager
import com.holeCode.novamoda.databinding.ActivityMainScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainScreenActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainScreenBinding
    private lateinit var mNavController: NavController
    private lateinit var adapterFragment: AdapterViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = DataBindingUtil.setContentView(this, R.layout.activity_main_screen)
        //==========================================================================
        //add nav controller
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavController = navHostFragment.navController
        //==========================================================================
        bindingMain.bottomNavigationView.setupWithNavController(mNavController)

    }
}