package com.holeCode.novamoda.auth

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.ActivityForgetPasswordBinding

class ForgetPasswordActivity : AppCompatActivity(), TextWatcher, View.OnClickListener {
    private lateinit var bindingForgetPasswordActivity: ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingForgetPasswordActivity = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(bindingForgetPasswordActivity.root)
        //================================================================================================
        bindingForgetPasswordActivity.edEmailforget.addTextChangedListener(this@ForgetPasswordActivity)
        val toolbar = bindingForgetPasswordActivity.forgetPasswrod
        toolbar.text = "Forget Password"
        setSupportActionBar(bindingForgetPasswordActivity.toolbarforget)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //===========================================================================================
        //handle setfocusListener
        emailFocusListener()


    } //end onCreate

    //=============================================================================================
    override fun onClick(view: View?) {
        if (view != null)
            when (view.id) {
                R.id.btn_forgetPasswrod -> {
                }
            }
    }

    //=============================================================================================
    //handle toolbar and button back page
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //=============================================================================================

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        bindingForgetPasswordActivity.btnSend.isEnabled =
            bindingForgetPasswordActivity.edEmailforget.text!!.trim().toString().isNotEmpty()
                    && validateEmail()
    }

    //this method validation email.
    private fun validateEmail(): Boolean {
        val email = bindingForgetPasswordActivity.edEmailforget.text.toString().trim()
        if (email.isEmpty()) {
            bindingForgetPasswordActivity.emailTilforget.error = "Email is required"
            bindingForgetPasswordActivity.emailTilforget.endIconDrawable = null
        } else if (!isValidationEmail(email)) {
            bindingForgetPasswordActivity.emailTilforget.error = "Invalid email address"
            bindingForgetPasswordActivity.emailTilforget.endIconDrawable = null
        } else {
            bindingForgetPasswordActivity.emailTilforget.apply {
                error = null
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return bindingForgetPasswordActivity.emailTilforget.error == null
    }

    private fun isValidationEmail(email: String): Boolean {
        val pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(pattern.toRegex())

    }

    private fun emailFocusListener() {
        val emailValue = bindingForgetPasswordActivity.edEmailforget
        emailValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmail()
            }
        }
    }


}