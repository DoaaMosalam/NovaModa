package com.holeCode.novamoda.auth

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.VibratorManager
import android.provider.CalendarContract.Colors
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.holeCode.novamoda.HomeScreenActivity
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.ActivityLoginBinding
import com.holeCode.novamoda.pojo.LoginBody
import com.holeCode.novamoda.pojo.RegisterBody
import com.holeCode.novamoda.repository.AuthRepository
import com.holeCode.novamoda.util.APIService
import com.holeCode.novamoda.view_model.LoginActivityViewModel
import com.holeCode.novamoda.view_model.LoginActivityViewModelFactory
import com.holeCode.novamoda.view_model.RegisterActivityViewModel
import com.holeCode.novamoda.view_model.RegisterActivityViewModelFactory

class LoginActivity : AppCompatActivity(), TextWatcher, View.OnClickListener,View.OnKeyListener {
    private lateinit var bindingLogActivity: ActivityLoginBinding
    private lateinit var checkIcon:Drawable
    private lateinit var mViewModel:LoginActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingLogActivity = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindingLogActivity.root)
        checkIcon=ContextCompat.getDrawable(this,R.drawable.baseline_check_24)!!
        //================================================================================================
        bindingLogActivity.edEmailLogin.addTextChangedListener(this@LoginActivity)
        bindingLogActivity.edPasswordLogin.addTextChangedListener(this@LoginActivity)
        //================================================================================================

        bindingLogActivity.btnLogin.setOnClickListener(this)
        bindingLogActivity.btnForgetPasswrod.setOnClickListener(this)

  //==================================================================================================
        //handle toolbar.
        val toolbar = bindingLogActivity.login
        toolbar.text = "Login"
        setSupportActionBar(bindingLogActivity.toolbarmain)
        supportActionBar?.title = ""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //========================================================================================
        //handle setFocusListener
        emailFocusListener()
        passwordFocusListener()
        //=========================================================================================
        mViewModel = ViewModelProvider(this, LoginActivityViewModelFactory(AuthRepository(APIService.getService()), application))
            .get(LoginActivityViewModel::class.java)
        setUpObserver()
    } //end onCreate
    //============================================================================================
    //handle toolbar button back previous page.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home->{
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    //==================================================================================================
    //handle onClickListener
    override fun onClick(view: View?) {
        if (view!=null ){
            when (view.id){
                R.id.btn_login ->{
                    onSubmitForm()
                }
                R.id.btn_forgetPasswrod->{
                    navigationToForgetPage()
                }
            }
        }
    }

    override fun onKey(view: View?, event: Int, keyEvent: KeyEvent?): Boolean {
        if (event==KeyEvent.KEYCODE_ENTER && keyEvent!!.action==KeyEvent.ACTION_UP) {
            onSubmitForm()
        }
        return false
    }
//==================================================================================================

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        bindingLogActivity.btnLogin.isEnabled =
            bindingLogActivity.edEmailLogin.text!!.trim().toString().isNotEmpty()
                    && bindingLogActivity.edPasswordLogin.text!!.trim().toString().isNotEmpty()
                    && validateEmail()
                    && validatePassword()
    }

    //==============================================================================================
    // invalidate email and password.
    private fun validateEmail(shouldUpdateView: Boolean = true):Boolean{
        val email = bindingLogActivity.edEmailLogin.text.toString().trim()
        if (email.isEmpty()){
            bindingLogActivity.emailTilLogin.error = "Email is required"
            bindingLogActivity.emailTilLogin.endIconDrawable = null
        }else if (!isValidationEmail(email)){
            bindingLogActivity.emailTilLogin.error = "Invalid email address"
            bindingLogActivity.emailTilLogin.endIconDrawable = null
        }else{
            bindingLogActivity.emailTilLogin.apply {
                error=null
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return bindingLogActivity.emailTilLogin.error==null
    }
    private fun isValidationEmail(email:String):Boolean{
        val pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return  email.matches(pattern.toRegex())

    }

    private fun validatePassword(): Boolean {
        val password = bindingLogActivity.edPasswordLogin.text.toString().trim()

        if (password.isEmpty()) {
            bindingLogActivity.passworrdTilLogin.error = "Password is required"
            bindingLogActivity.passworrdTilLogin.endIconDrawable = null
        } else if (password.length < 8) {
            bindingLogActivity.passworrdTilLogin.error = "Password must be at least 8 characters"
            bindingLogActivity.passworrdTilLogin.endIconDrawable = null
        } else if (!password.matches(".*[A-Z].*".toRegex())) {
            bindingLogActivity.passworrdTilLogin.error =
                "Password must contain 1 upper-case character"
            bindingLogActivity.passworrdTilLogin.endIconDrawable = null
        } else if (!password.matches(".*[a-z].*".toRegex())) {
            bindingLogActivity.passworrdTilLogin.error =
                "Password must contain 1 lower-case character"
        } else if (!password.matches(".*[@#\$%^&+=].*".toRegex())) {
            bindingLogActivity.passworrdTilLogin.error =
                "Password must contain special[@#\$%^&+=] "
        } else if (!password.matches(".*[1-9]|10.*".toRegex())) {
            bindingLogActivity.passworrdTilLogin.error ="Password must contains numbers 1:10"
        } else {
            bindingLogActivity.passworrdTilLogin.apply {
                error = null
                endIconDrawable = checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return  bindingLogActivity.passworrdTilLogin.error == null
    }

    private fun emailFocusListener() {
        val emailValue = bindingLogActivity.edEmailLogin
        emailValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmail()
            }
//            else if (!hasFocus) {
//                mViewModel.validateEmailAddress(ValidateEmailBody(emailValue.toString()))
//            }
        }
    }

    private fun passwordFocusListener() {
        val passwordValue = bindingLogActivity.edPasswordLogin
        passwordValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePassword()
            }
        }
    }
    //==============================================================================================
    private fun setUpObserver() {
        mViewModel.getIsLoading().observe(this) {
            bindingLogActivity.progressbarlogin.isVisible = it
        }

        mViewModel.getErrorMessage().observe(this) {
            //Name,Phone,Email,Password
            val formErrorKey = arrayOf( "email", "password")
            val message = StringBuilder()
            it.map { entry ->
                if (formErrorKey.contains(entry.key)) {
                    when (entry.key) {

                        "email" -> {
                            bindingLogActivity.emailTilLogin.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "password" -> {
                            bindingLogActivity.passworrdTilLogin.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }
                    }
                } else {
                    message.append(entry.value).append("\n")
                }
                if (message.isNotEmpty()) {
                    AlertDialog.Builder(this)
                        .setIcon(R.drawable.baseline_info_24)
                        .setTitle("INFORMATION")
                        .setMessage(message)
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            }
        }
        mViewModel.getUser().observe(this) {
            if (it != null) {
                navigateGoToHome()
            }
        }
    }

    private fun onSubmitForm(){
        if (validate()) {
            //make Api request.
            mViewModel.loginUserVM(
                LoginBody(
                    bindingLogActivity.edEmailLogin.text.toString(),
                    bindingLogActivity.edPasswordLogin.text.toString()
                )
            )
        }
        navigateGoToHome()
    }

    /*This method validate all field edit text */
    private fun validate(): Boolean {
        var isvalide = true
        if (!validateEmail(shouldUpdateView = false)) isvalide = false
        if (!validatePassword()) isvalide = false
        return isvalide

    }

    //==============================================================================================
    private fun navigationToForgetPage() {
        startActivity(Intent(this@LoginActivity, ForgetPasswordActivity::class.java))
    }
    private fun navigateGoToHome() {
        val intent = Intent(this@LoginActivity, HomeScreenActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }



}