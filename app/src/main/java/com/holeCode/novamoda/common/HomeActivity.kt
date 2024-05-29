package com.holeCode.novamoda.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.doaamosallam.domain.model.products.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    private val viewModel:HomeActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val user = intent.getParcelableExtra<UserData>("User")
//        authorization = user!!.token!!
//        Log.d("token", authorization)
//        //val modelFactory = HomeViewModelFactory(application, lang, authorization)
//        //viewModel = ViewModelProvider(this)[HomeActivityViewModel::class.java]
//        viewModel.setUser(user)


        // احصل على المستخدم من الانتباه
        val user = intent.getParcelableExtra<UserData>("User")
        if (user != null) {
            viewModel.setUser(user)
        }


        val navController = findNavController(R.id.home_nav)
        binding.bottomNavigation.setupWithNavController(navController)
        // appear label when to click icon
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            binding.bottomNavigation.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_SELECTED
            navController.navigate(item.itemId)
            true
        }


        viewModel.language.observe(this) { language ->
            setLocal(this, language)
        }

        viewModel.logOut.observe(this) { isLoggedOut ->
            if (isLoggedOut) {
                removeUser()
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(intent)
                finish()
            }
        }

        viewModel.cart.observe(this) { cartModel ->
            val cartItems = cartModel.data?.cart_items ?: emptyList()
            viewModel.setBadge(cartItems.size)
        }

        viewModel.badge.observe(this) { badgeNumber ->
            val badge = binding.bottomNavigation.getOrCreateBadge(R.id.bagFragment)
            if (badgeNumber > 0) {
                badge.isVisible = true
                badge.number = badgeNumber
            } else {
                badge.isVisible = false
            }
        }
    }

    private fun closeKeyBoard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun removeUser() {
        val sharedPreferences = application.getSharedPreferences("MyShared", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("name", null)
            putString("email", null)
            putString("phone", null)
            putString("token", null)
            apply()
        }
    }



}
