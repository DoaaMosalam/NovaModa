package com.holeCode.novamoda.common

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    abstract val bindingInflater:(LayoutInflater) -> VB

    private lateinit  var _binding:VB
    protected val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    _binding = bindingInflater(layoutInflater)

    setContentView(_binding.root)
    }
}