package com.holeCode.novamoda.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.holeCode.novamoda.R
import com.holeCode.novamoda.common.BaseFragment
import com.holeCode.novamoda.common.HomeActivityViewModel
import com.holeCode.novamoda.common.MainActivity
import com.holeCode.novamoda.common.lang
import com.holeCode.novamoda.common.saveLang
import com.holeCode.novamoda.databinding.FragmentProfileBinding
import com.holeCode.novamoda.ui.fragments.change_password.ChangePasswordFragment
import com.holeCode.novamoda.ui.fragments.forget_password.ForgetPasswordFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class ProfileFragment :BaseFragment<FragmentProfileBinding,ProfileViewModel>() {
    override fun getLayoutResID() = R.layout.fragment_profile
    override val viewModel: ProfileViewModel by viewModels()
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitView()
    }

    override fun setUpInitView() {
        binding.lifecycleOwner = this
        binding.user = viewModel

        sharedViewModel.language.observe(viewLifecycleOwner) {
            saveLang(requireActivity().application, it)
            if (it == "ar")
                binding.spinnerLang.setSelection(1)
            else
                binding.spinnerLang.setSelection(0)

            if (lang != it) {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
            }
            lang = it
        }
        viewModel.showCancel.observe(viewLifecycleOwner) {
            enabled(binding.btnCancelAccount, it)
        }
        viewModel.showEdit.observe(viewLifecycleOwner) {
            enabled(binding.btnChange, it)
        }
        viewModel.showEdit2.observe(viewLifecycleOwner) {
            enabled(binding.btnChange2, it)
        }
        viewModel.showConsUp.observe(viewLifecycleOwner) {
            enabled(binding.consUP, it)
        }
        viewModel.showConsDown.observe(viewLifecycleOwner) {
            enabled(binding.consDown, it)
            enabled(binding.spinnerLang, it)
            enabled(binding.btnLogOut, it)
            enabled(binding.btnFavorite, it)
            enabled(binding.btnChangePassword, it)
            enableEditTexts(binding.edtEmailAccount, !it)
            enableEditTexts(binding.edtNameAccount, !it)
            enableEditTexts(binding.edtPhoneAccount, !it)
            //binding.consDown.isClickable = false
        }

        sharedViewModel.user.observe(viewLifecycleOwner) {
            viewModel.setData(it)
        }

        viewModel.changeUser.observe(viewLifecycleOwner) {
            if (it) {
                sharedViewModel.setUser(viewModel.user.value!!)
                viewModel.changeUserDone()
            }
        }

        viewModel.edtPhone.observe(viewLifecycleOwner) {
            if (it != viewModel.user.value!!.phone)
                viewModel.setEdit2Show(true)
            else
                viewModel.setEdit2Show(false)
        }
        viewModel.edtEmail.observe(viewLifecycleOwner) {
            if (it != viewModel.user.value!!.email)
                viewModel.setEdit2Show(true)
            else
                viewModel.setEdit2Show(false)
        }
        viewModel.edtName.observe(viewLifecycleOwner) {
            if (it != viewModel.user.value!!.name)
                viewModel.setEdit2Show(true)
            else
                viewModel.setEdit2Show(false)
        }


        binding.btnLogOut.setOnClickListener {
            sharedViewModel.logOut()
        }
//        sharedViewModel.navigateToSearch.observe(viewLifecycleOwner) {
//            if (it) {
//                findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToSearchFragment())
//                sharedViewModel.navigateToSearchDone()
//            }
//        }

        val adapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerLang.adapter = adapter
        binding.spinnerLang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0)
                    sharedViewModel.setLang("en")
                else
                    sharedViewModel.setLang("ar")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        viewModel.navigateToFavorite.observe(viewLifecycleOwner) {
            if (it) {
                navigateToFavorite()
                viewModel.navigateToFavoriteDone()
            }
        }

        viewModel.navigateToChangePass.observe(viewLifecycleOwner) {
            if (it) {
                navigateToChangePassword()
                viewModel.navigateToChangePassDone()
            }
        }
    }
    private fun enabled(view: View, boolean: Boolean) {
        if (boolean) {
            view.isEnabled = true
            view.alpha = 1f
        } else {
            view.isEnabled = false
            view.alpha = 0.25f
        }
    }

    private fun enableEditTexts(view: View, boolean: Boolean) {
        view.isEnabled = boolean
    }

    private fun navigateToFavorite(){
        binding.btnFavorite.setOnClickListener { v->
            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_favoriteFragment)
//            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFavoriteFragment())
        }

    }

    private fun navigateToChangePassword() {
        binding.btnChangePassword.setOnClickListener {
            showChangePasswordBottomSheet()
        }
    }

    private fun showChangePasswordBottomSheet() {
        val changePasswordFragment = ChangePasswordFragment()
        changePasswordFragment.show(parentFragmentManager, changePasswordFragment.tag)
    }

}