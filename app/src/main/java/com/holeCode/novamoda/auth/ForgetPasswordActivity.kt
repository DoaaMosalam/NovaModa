package com.holeCode.novamoda.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher

import com.holeCode.novamoda.databinding.ActivityForgetPasswordBinding

class ForgetPasswordActivity : AppCompatActivity(), TextWatcher {
    private lateinit var bindingForgetPasswordActivity: ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingForgetPasswordActivity = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(bindingForgetPasswordActivity.root)
        //================================================================================================
        bindingForgetPasswordActivity.edEmailforget.addTextChangedListener(this@ForgetPasswordActivity)

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        bindingForgetPasswordActivity.btnSend.isEnabled =
            bindingForgetPasswordActivity.edEmailforget.text!!.trim().toString().isNotEmpty()
    }
}