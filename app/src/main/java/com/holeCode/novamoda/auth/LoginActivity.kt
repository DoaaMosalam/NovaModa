package com.holeCode.novamoda.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.holeCode.novamoda.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var bindingLogActivity:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingLogActivity = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindingLogActivity.root)

        bindingLogActivity.btnLogin.setOnClickListener {
            navigationToForgetPage()

        }
    }

    private fun navigationToForgetPage(){
        startActivity(Intent(this@LoginActivity,ForgetPasswordActivity::class.java))
    }
}