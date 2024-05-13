package com.holeCode.novamoda.ui.splashScreen

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.holeCode.novamoda.common.BasicActivity
import com.holeCode.novamoda.common.HomeActivity
import com.holeCode.novamoda.R
import com.holeCode.novamoda.data.local.UserPreferencesDataSource
import com.holeCode.novamoda.data.repository.auth.UserViewModel
import com.holeCode.novamoda.data.repository.auth.UserViewModelFactory
import com.holeCode.novamoda.data.repository.user.UserDataStoreRepositoryImpl
import com.holeCode.novamoda.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BasicActivity<ActivitySplashBinding>() {
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserDataStoreRepositoryImpl(UserPreferencesDataSource(this)))
    }

    override fun getLayoutResId() = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        initSplashScreen()
        super.onCreate(savedInstanceState)


        lifecycleScope.launch(Dispatchers.Main) {
            val isLoggedIn = userViewModel.isUserLoggedIn().first()
            Log.d(ContentValues.TAG, "onCreate: isLoggedIn: $isLoggedIn")
            if (isLoggedIn) {
                setContentView(R.layout.activity_main)
            } else {
                userViewModel.setIsLoggedIn(true)
                goToAuthActivity()
            }
        }
    }


    private fun goToAuthActivity() {
        val intent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val options = ActivityOptions.makeCustomAnimation(
            this, android.R.anim.fade_in, android.R.anim.fade_out
        )
        startActivity(intent, options.toBundle())
    }

    // This method is used to hide the status bar and make the splash screen as a full screen activity.
    private fun initSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenViewProvider.view,
                    "translationY",
                    0f,
                    -splashScreenViewProvider.view.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 1000L
                slideUp.doOnEnd { splashScreenViewProvider.remove() }
                //Run the animation.
                slideUp.start()
            }
        } else {
            setTheme(R.style.Theme_NovaModa)

        }
    }
}