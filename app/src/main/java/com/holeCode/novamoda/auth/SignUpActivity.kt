package com.holeCode.novamoda.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var bindingSingUpActivity: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSingUpActivity = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(bindingSingUpActivity.root)
        bindingSingUpActivity.btnSignUp.setOnClickListener {
            navigationToLoginPage()
        }


    }

    private fun navigationToLoginPage(){
        startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
    }
}