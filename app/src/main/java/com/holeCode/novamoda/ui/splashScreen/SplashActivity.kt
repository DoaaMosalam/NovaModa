package com.holeCode.novamoda.ui.splashScreen

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.holeCode.novamoda.R
import com.holeCode.novamoda.common.BaseActivity
import com.holeCode.novamoda.common.HomeActivity
import com.holeCode.novamoda.data.repository.auth.UserViewModel
import com.holeCode.novamoda.databinding.ActivitySplashBinding
import com.holeCode.novamoda.ui.fragments.login.LoginFragment.Companion.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        initSplashScreen()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val isLoggedIn = userViewModel.isUserLoggedIn().first()
            Log.d(TAG, "onCreate: isLoggedIn: $isLoggedIn")
            if (isLoggedIn) {
                setContentView(R.layout.activity_main)
            } else {
                userViewModel.setIsLoggedIn(true)
                goToHomeActivity()
            }
        }
    }
    private fun goToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val options = ActivityOptions.makeCustomAnimation(
            this, android.R.anim.fade_in, android.R.anim.fade_out
        )
        startActivity(intent, options.toBundle())
        finish()
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
