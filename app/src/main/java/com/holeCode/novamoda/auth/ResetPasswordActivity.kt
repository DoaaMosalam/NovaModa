package com.holeCode.novamoda.auth

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.ActivityResetPasswordBinding
import com.holeCode.novamoda.storage.Firebasedb
import kotlinx.coroutines.launch

class ResetPasswordActivity : AppCompatActivity(), TextWatcher, View.OnClickListener,
    View.OnKeyListener {
    private lateinit var bindingResetPassword: ActivityResetPasswordBinding
    private lateinit var checkIcon: Drawable
    private var firebasedb: Firebasedb = Firebasedb()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingResetPassword = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(bindingResetPassword.root)
        checkIcon = ContextCompat.getDrawable(this, R.drawable.baseline_check_24)!!
        //==========================================================
        //handle toolbar.
        val toolbar = bindingResetPassword.forgetPasswrod
        toolbar.text = getString(R.string.forget_password)
        setSupportActionBar(bindingResetPassword.toolbarforget)
        supportActionBar?.title = ""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //=================================================================
        emailFocusListener()
        bindingResetPassword.edEmailforget.addTextChangedListener(this@ResetPasswordActivity)
        //====================================================================
        bindingResetPassword.btnSend.setOnClickListener(this)
        //=====================================================================

    } // end on create

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
        bindingResetPassword.btnSend.isEnabled =
            bindingResetPassword.edEmailforget.text!!.trim().toString().isNotEmpty()
                    && validateEmail()
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btn_send -> {
                    sendNameEmail()
                    onSubmit()
//                    if (view != null) {
//                        sendNameEmail()
//                    } else {
//                        onSubmit()
//                    }
                }
            }
        }
    }

    /*This code validate email when user write email should correct write code*/
    private fun validateEmail(): Boolean {
        val email = bindingResetPassword.edEmailforget.text.toString().trim()
        if (email.isEmpty()) {
            bindingResetPassword.emailTilforget.error = "Email is required"
            bindingResetPassword.emailTilforget.endIconDrawable = null
        } else if (!isValidationEmail(email)) {
            bindingResetPassword.emailTilforget.error = "Invalid email address"
            bindingResetPassword.emailTilforget.endIconDrawable = null
        } else {
            bindingResetPassword.emailTilforget.apply {
                error = null
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return bindingResetPassword.emailTilforget.error == null
    }

    private fun isValidationEmail(email: String): Boolean {
        val pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(pattern.toRegex())
    }

    private fun emailFocusListener() {
        val emailForget = bindingResetPassword.edEmailforget
        emailForget.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmail()
            }
        }
    }

    //this method send email other class.
    private fun sendNameEmail() {
        val email = bindingResetPassword.edEmailforget.text.toString()
        val intent = Intent(this, UpdatePasswordActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }

    private fun validate(): Boolean {
        var isValidate = true
        if (!validateEmail()) isValidate = false
        return isValidate
    }

    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        if (KeyEvent.KEYCODE_ENTER == keyCode && keyEvent!!.action == KeyEvent.ACTION_UP) {
//            do reset password
            onSubmit()
        }
        return false
    }

    private fun onSubmit() {
        if (validate()) {
            lifecycleScope.launch {
                firebasedb.resetPasswordByFirebase(
                    bindingResetPassword.edEmailforget.text.toString()
                )
            }
        }
    }
//        startActivity(Intent(this@ResetPasswordActivity, UpdatePasswordActivity::class.java))
}
