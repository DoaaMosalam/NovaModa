package com.holeCode.novamoda.ui.fragments.change_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.FragmentChangePasswordBinding
import com.holeCode.novamoda.databinding.FragmentForgetPasswordBinding
import com.holeCode.novamoda.ui.fragments.forget_password.ForgetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePasswordFragment : BottomSheetDialogFragment() {

    private val viewModel: ChangePasswordViewModel by viewModels()

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomNav: BottomNavigationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)


        binding.lifecycleOwner = viewLifecycleOwner
        binding.model = viewModel

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitView()

    }
    private fun setUpInitView() {
        lifecycleScope.launch {
            viewModel.errorMessage.collect { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnChangePasswordChange.setOnClickListener {
            val currentPass = binding.edtCurrentPassword.text.toString()
            val newPass = binding.edtNewPassword.text.toString()

            viewModel.currentPass.value = currentPass
            viewModel.newPass.value = newPass
            viewModel.changePass()
        }
    }

//    private fun observeViewModel() {
//        lifecycleScope.launch {
//            viewModel.errorMessage.collect { message ->
//                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}