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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.ActivityResetPasswordBinding
import com.holeCode.novamoda.pojo.ResetPasswordBody
import com.holeCode.novamoda.repository.AuthRepository
import com.holeCode.novamoda.storage.FirebaseAuthenticationManager
import com.holeCode.novamoda.util.APIService
import com.holeCode.novamoda.view_model.ResetActivityViewModel
import com.holeCode.novamoda.view_model.ResetActivityViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ResetPasswordActivity : AppCompatActivity(), TextWatcher, View.OnClickListener,
    View.OnKeyListener {
    private lateinit var bindingResetPassword: ActivityResetPasswordBinding
    private lateinit var checkIcon: Drawable
    private lateinit var mViewModel: ResetActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingResetPassword = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(bindingResetPassword.root)
        checkIcon = ContextCompat.getDrawable(this, R.drawable.baseline_check_24)!!
        //==========================================================
        //handle toolbar.
        val toolbar = bindingResetPassword.forgetPasswrod
        toolbar.text = "Reset Password"
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

//        mViewModel = ViewModelProvider(
//            this,
//            ResetActivityViewModelFactory(AuthRepository(APIService.getService()), application)
//        )
//            .get(ResetActivityViewModel::class.java)
//        setUpObserver()
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
                    if (view != null) {
                        sendNameEmail()
                    } else {
                        onSubmit()
                    }
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

    //    private fun setUpObserver() {
//        mViewModel.getIsLoading().observe(this) {
//            bindingResetPassword.progressbarforget.isVisible = it
//        }
//        mViewModel.getErrorMessage().observe(this) {
//            //Name,Phone,Email,Password
//            val formErrorKey = arrayOf("email")
//            val message = StringBuilder()
//            it.map { entry ->
//                if (formErrorKey.contains(entry.key)) {
//                    when (entry.key) {
//                        "email" -> {
//                            bindingResetPassword.emailTilforget.apply {
//                                isErrorEnabled = true
//                                error = entry.value
//                            }
//                        }
//                    }
//                } else {
//                    message.append(entry.value).append("\n")
//                }
//                if (message.isNotEmpty()) {
//                    AlertDialog.Builder(this)
//                        .setIcon(R.drawable.baseline_info_24)
//                        .setTitle("INFORMATION")
//                        .setMessage(message)
//                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
//                        .show()
//                }
//            }
//        }
//        mViewModel.getUser().observe(this) {
//            if (it != null) {
//                startActivity(Intent(this@ResetPasswordActivity,UpdatePasswordActivity::class.java))
//            }
//        }
//    }
//
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
//            mViewModel.resetPasswordVM(
//                ResetPasswordBody(
//                bindingResetPassword.edEmailforget.text.toString())
//            )
        }
        mViewModel.resetPasswordByFirebase(bindingResetPassword.edEmailforget.text.toString())
        startActivity(Intent(this@ResetPasswordActivity, UpdatePasswordActivity::class.java))
    }
}
