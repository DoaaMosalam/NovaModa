package com.holeCode.novamoda.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.holeCode.novamoda.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity(),TextWatcher {
    private lateinit var bindingSingUpActivity: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSingUpActivity = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(bindingSingUpActivity.root)
        //================================================================================================
        bindingSingUpActivity.edNameSign.addTextChangedListener(this@SignUpActivity)
        bindingSingUpActivity.edEmailSign.addTextChangedListener(this@SignUpActivity)
        bindingSingUpActivity.edPasswordSing.addTextChangedListener(this@SignUpActivity)
  //================================================================================================
        bindingSingUpActivity.btnSignUp.setOnClickListener {
            navigationToLoginPage()
        }


    }

    private fun navigationToLoginPage(){
        startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
    }
    //================================================================================================
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
    override fun afterTextChanged(p0: Editable?) {
        bindingSingUpActivity.btnSignUp.isEnabled = bindingSingUpActivity.edNameSign.text!!.trim().isNotEmpty()
                && bindingSingUpActivity.edEmailSign.text!!.trim().isNotEmpty()
                && bindingSingUpActivity.edPasswordSing.text!!.trim().isNotEmpty()
    }
}