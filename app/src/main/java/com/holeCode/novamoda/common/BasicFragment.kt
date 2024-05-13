package com.holeCode.novamoda.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.holeCode.novamoda.view.ProgressDialog
import androidx.annotation.LayoutRes

abstract class BasicFragment<DB : ViewDataBinding,VM:ViewModel> : Fragment() {
     val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }
    protected abstract val viewModel: VM
    protected var _binding: DB? = null
    protected val binding get() = _binding!!

//    protected lateinit var binding: DB
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutResID(), container, false)
        return binding.root
    }
    /**
     * Get layout resource id which inflate in onCreateView.
     */
    @LayoutRes
    abstract fun getLayoutResID(): Int
    abstract fun setUpObserve()

}