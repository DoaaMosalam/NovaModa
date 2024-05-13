package com.holeCode.novamoda.common

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.ActivityHomeBinding

class HomeActivity : BasicActivity<ActivityHomeBinding>() {
    override fun getLayoutResId() = R.layout.activity_home
    private lateinit var mNavController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavController = navHostFragment.navController


        bindingApp.bottomNavigation.setupWithNavController(mNavController)

    }
}