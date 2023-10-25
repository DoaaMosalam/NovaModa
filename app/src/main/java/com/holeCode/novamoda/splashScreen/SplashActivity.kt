package com.holeCode.novamoda.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.holeCode.novamoda.MainScreenActivity
import com.holeCode.novamoda.auth.SignUpActivity
import com.holeCode.novamoda.databinding.ActivitySplashBinding
import com.holeCode.novamoda.storage.SharedPreferencesManager

class SplashActivity : AppCompatActivity() {
    private lateinit var splashBinding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

        checkSharedPrefs()
        //==========================================================================================
        splashBinding.motionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float,
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                startActivity(Intent(this@SplashActivity, SignUpActivity::class.java))
                finish()
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float,
            ) {

            }

        })
    }

    //==================================================================================================
    fun checkSharedPrefs() {
        val sharedPreferencesManager = SharedPreferencesManager.getInstance(this)
        val isRegister = sharedPreferencesManager.isUserRegisterIn()

        if (isRegister) {
            // User is register in, navigate to the home page
            startActivity(Intent(this, MainScreenActivity::class.java))
            finish()
        } else {
            // User is not register in, navigate to the login page
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }
}