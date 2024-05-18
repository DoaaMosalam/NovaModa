package com.holeCode.novamoda.common

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val navController = findNavController(R.id.home_nav)
        binding.bottomNavigation.setupWithNavController(navController)
    }
}