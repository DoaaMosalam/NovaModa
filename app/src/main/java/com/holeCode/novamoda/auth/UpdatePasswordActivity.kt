package com.holeCode.novamoda.auth

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.holeCode.novamoda.HomeScreenActivity
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.ActivityUpdatePasswordBinding

class UpdatePasswordActivity : AppCompatActivity(), View.OnClickListener,TextWatcher {
    private lateinit var bindingNewPassword: ActivityUpdatePasswordBinding
    private lateinit var checkIcon: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingNewPassword = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(bindingNewPassword.root)
        //==========================================================================================
        checkIcon = ContextCompat.getDrawable(this, R.drawable.baseline_check_24)!!
        //call method Retrieve email from forget password activity class
        retrieveEmail()

        bindingNewPassword.apply {
            bindingNewPassword.edCodeNewPassword.addTextChangedListener(this@UpdatePasswordActivity)
            bindingNewPassword.ednewPassword.addTextChangedListener(this@UpdatePasswordActivity)
        }
//==================================================================================================
        bindingNewPassword.btnConfirm.setOnClickListener(this)
//=================================================================================================
        // handle toolbar
        val toolbar = findViewById<TextView>(R.id.txt_newPassword)
        toolbar.text = "Create New Password"
        setSupportActionBar(bindingNewPassword.toolbarNewpassword)
        supportActionBar?.title = ""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //=============================================================
        passwordFocusListener()
        codeFocusListener()
        //===================================================================================

    }//end onCreate
    //This method to retrieve mail from reset password activity class.
    private fun retrieveEmail(){
        val email= intent.getStringExtra("email")
        if(email!=null){
            bindingNewPassword.txtEmail.text = email
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) {
        bindingNewPassword.btnConfirm.isEnabled=
            bindingNewPassword.edCodeNewPassword.text!!.trim().toString().isNotEmpty()
                    && bindingNewPassword.ednewPassword.text!!.trim().toString().isNotEmpty()
                    &&validateCode()
                    &&validateNewPassword()
    }


    // this is method implementation onClickListener
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btn_confirm -> {

                   navigateGoToHome()
                }
            }
        }
    }

    // this method validate password
    private fun validateNewPassword(): Boolean {
        val password = bindingNewPassword.ednewPassword.text.toString().trim()
        if (password.isEmpty()) {
            bindingNewPassword.newpasswordTil.error = "Password is required"
            bindingNewPassword.newpasswordTil.endIconDrawable = null
        } else if (password.length < 8) {
            bindingNewPassword.newpasswordTil.error = "Password must be at least 8 characters"
            bindingNewPassword.newpasswordTil.endIconDrawable = null
        } else if (!password.matches(".*[A-Z].*".toRegex())) {
            bindingNewPassword.newpasswordTil.error =
                "Password must contain 1 upper-case character"
            bindingNewPassword.newpasswordTil.endIconDrawable = null
        } else if (!password.matches(".*[a-z].*".toRegex())) {
            bindingNewPassword.newpasswordTil.error =
                "Password must contain 1 lower-case character"
        } else if (!password.matches(".*[@#\$%^&+=].*".toRegex())) {
            bindingNewPassword.newpasswordTil.error =
                "Password must contain special[@#\$%^&+=] "
        } else if (!password.matches(".*[1-9]|10.*".toRegex())) {
            bindingNewPassword.newpasswordTil.error =
                "Password must contains numbers 1:10"
        } else {
            bindingNewPassword.newpasswordTil.apply {
                error = null
                endIconDrawable = checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return bindingNewPassword.newpasswordTil.error == null
    }

    private fun passwordFocusListener() {
        val passwordValue = bindingNewPassword.ednewPassword
        passwordValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateNewPassword()
            }
        }
    }

    //=================================================================================
// this method validate password
    private fun validateCode(): Boolean {
        val code = bindingNewPassword.edCodeNewPassword.text.toString().trim()

        if (code.isEmpty()) {
            bindingNewPassword.codeTil.error = "Password is required"
            bindingNewPassword.codeTil.endIconDrawable = null

        } else {
            bindingNewPassword.codeTil.apply {
                error = null
                endIconDrawable = checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return bindingNewPassword.codeTil.error == null

    }

    private fun codeFocusListener() {
        val codeValue = bindingNewPassword.edCodeNewPassword
        codeValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateCode()
            }
        }
    }

    //===============================================================================


    private fun navigateGoToHome() {
        startActivity(Intent(this@UpdatePasswordActivity,HomeScreenActivity::class.java))
    }


}

