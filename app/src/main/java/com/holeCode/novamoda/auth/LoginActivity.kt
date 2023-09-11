package com.holeCode.novamoda.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.holeCode.novamoda.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() ,TextWatcher{
    private lateinit var bindingLogActivity:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingLogActivity = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindingLogActivity.root)
        //================================================================================================
        bindingLogActivity.edEmailLogin.addTextChangedListener(this@LoginActivity)
        bindingLogActivity.edPasswordLogin.addTextChangedListener(this@LoginActivity)
        //================================================================================================
        bindingLogActivity.apply {
            btnForgetPasswrod.setOnClickListener {
                navigationToForgetPage()

            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
    override fun afterTextChanged(p0: Editable?) {
        bindingLogActivity.btnLogin.isEnabled = bindingLogActivity.edEmailLogin.text!!.trim().toString().isNotEmpty()
                && bindingLogActivity.edPasswordLogin.text!!.trim().toString().isNotEmpty()
    }
    //==============================================================================================
    private fun navigationToForgetPage(){
        startActivity(Intent(this@LoginActivity,ForgetPasswordActivity::class.java))
    }
}