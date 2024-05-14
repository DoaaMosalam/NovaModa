package com.holeCode.novamoda.ui.fragments.forget_password

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.holeCode.novamoda.R
import com.holeCode.novamoda.data.model.Resource
import com.holeCode.novamoda.databinding.FragmentForgetPasswordBinding
import com.holeCode.novamoda.util.isEmailValid
import com.holeCode.novamoda.util.showSnakeBarError
import com.holeCode.novamoda.view.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgetPasswordFragment :  BottomSheetDialogFragment(), TextWatcher {

    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    private val viewModel: ForgetPasswordViewModel by viewModels()

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var checkIcon: Drawable


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forget_password, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.resetViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkIcon = ContextCompat.getDrawable(requireActivity(), R.drawable.baseline_check_24)!!
        binding.edEmailforget.addTextChangedListener(this)
        emailFocusListener()
        initViewModel()

    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.forgetPasswordState.collect{state->
                when(state){
                    is Resource.Loading->{
                        progressDialog.show()
                    }
                    is Resource.Success->{
                        progressDialog.dismiss()
                        showSentEmailSuccessDialog()
                    }
                    is Resource.Error->{
                        progressDialog.dismiss()
                        val msg = state.exception?.message?: getString(R.string.generic_error_msg)
                        Log.d(TAG, "initViewModelError: $msg")
                        view?.showSnakeBarError(msg)
                    }

                }
            }

        }
    }
    private fun showSentEmailSuccessDialog() {
        MaterialAlertDialogBuilder(requireActivity()).setTitle("Reset Password")
            .setMessage("We have sent you an email to reset your password. Please check your email.")
            .setPositiveButton(
                "OK"
            ) { dialog, _ ->
                dialog?.dismiss()
//                this@ForgetPasswordFragment.dismiss()
                navigateToLoginFragment()

            }
                .create()
                .show()
    }
    private fun navigateToLoginFragment() {
        val navController = findNavController() ?: return // Ensure NavController is not null
        if (navController.currentDestination?.id == R.id.registerFragment) {
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ForgetPasswordFragment"
    }

    //====================================================================================
    private fun emailFocusListener() {
        val emailValue = binding.edEmailforget
        emailValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmail()
            }
        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.edEmailforget.text.toString().trim()
        if (!email.isEmailValid()) {
            binding.emailTilforget.error = getString(R.string.invalid_email_address)
            binding.emailTilforget.endIconDrawable = null
        } else {
            binding.emailTilforget.apply {
                error = null
                endIconDrawable = checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return binding.emailTilforget.error == null
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        binding.btnSend.isEnabled =
            binding.edEmailforget.text!!.trim().isNotEmpty()
                    && validateEmail()

    }
}