package com.holeCode.novamoda.common

import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var mNavController: NavController
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding = ActivityMainBinding::inflate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavController = navHostFragment.navController

    }
}