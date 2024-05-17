package com.holeCode.novamoda.common

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.ActivityHomeBinding
import com.holeCode.novamoda.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
            = ActivityHomeBinding::inflate

    private lateinit var mNavController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_nav) as NavHostFragment
        mNavController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)

//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.home_nav) as NavHostFragment
//        mNavController = navHostFragment.navController
//
//        val bottomView : BottomNavigationView = findViewById(R.id.bottomNavigation)
//        bottomView.setupWithNavController(mNavController)


    }
}