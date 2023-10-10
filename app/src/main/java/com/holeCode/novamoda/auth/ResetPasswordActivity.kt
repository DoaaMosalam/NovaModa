package com.holeCode.novamoda.auth

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.ActivityResetPasswordBinding
import com.holeCode.novamoda.view_model.ResetActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResetPasswordActivity : AppCompatActivity(), TextWatcher, View.OnClickListener {
    private lateinit var bindingResetPassword: ActivityResetPasswordBinding
    private lateinit var checkIcon: Drawable
    private lateinit var mViewModel: ResetActivityViewModel
    private lateinit var mAuth: FirebaseAuth
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
//        ).get(ResetActivityViewModel::class.java)
//
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
                    onSubmit()
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
//        mViewModel.getIsLoading().observe(this){
//            bindingResetPassword.progressbarforget.isVisible=it
//        }
//        mViewModel.getErrorMessage().observe(this){
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
//                if (message.isEmpty()) {
//                    AlertDialog.Builder(this)
//                        .setIcon(R.drawable.baseline_info_24)
//                        .setTitle("INFORMATION")
//                        .setMessage(message)
//                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
//                        .show()
//                }
//            }
//        }
//        mViewModel.getUser().observe(this){
//            if (it!=null){
//                sendNameEmail()
//                startActivity(Intent(this@ResetPasswordActivity,UpdatePasswordActivity::class.java))
//            }
//        }
//    }

    private fun validate(): Boolean {
        var isValidate = true
        if (!validateEmail()) isValidate = false
        return isValidate
    }

    private fun onSubmit() {
        lifecycleScope.launch {
            if (validate()) {
//                                createAndSendResetEmail(bindingResetPassword.edEmailforget)
                sendResetEmail(bindingResetPassword.edEmailforget)

            }
        }
        sendNameEmail()
//        if (validate()) {
//            mViewModel.resetPasswordVM(ResetPasswordBody(
//                bindingResetPassword.edEmailforget.text.toString())
//            )
////            sendNameEmail()
//        }
//        startActivity(Intent(this@ResetPasswordActivity,UpdatePasswordActivity::class.java))
    }

    private suspend fun createAndSendResetEmail(email: EditText) {
        mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(email.toString(), "").addOnCompleteListener { task ->
            if (task.isSuccessful) {
                lifecycleScope.launch {
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        "Successful Create Email:  ",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    private suspend fun sendResetEmail(email: EditText) {
        mAuth = FirebaseAuth.getInstance()
        mAuth.sendPasswordResetEmail(email.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Open Gmail", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    openGmail()
                }
            }

        }

    }

    //Add method to open gmail.
    private suspend fun openGmail() {
        lifecycleScope.launch {
            try {
                // Open Gmail app
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://mail.google.com")
                }

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            } catch (e: Exception) {
                Toast.makeText(this@ResetPasswordActivity, e.message, Toast.LENGTH_SHORT).show()
            }

        }

    }

}
