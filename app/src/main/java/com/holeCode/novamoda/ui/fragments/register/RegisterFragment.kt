package com.holeCode.novamoda.ui.fragments.register

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope

import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.holeCode.novamoda.R
import com.holeCode.novamoda.common.BaseFragment
import com.holeCode.novamoda.data.model.Resource
import com.holeCode.novamoda.databinding.FragmentRegisterBinding
import com.holeCode.novamoda.ui.fragments.login.LoginFragment
import com.holeCode.novamoda.util.isEmailValid
import com.holeCode.novamoda.util.isNameValid
import com.holeCode.novamoda.util.isPasswordValid
import com.holeCode.novamoda.util.isPhoneValid
import com.holeCode.novamoda.util.showSnakeBarError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding,RegisterViewModel>(), TextWatcher {
    override fun getLayoutResID() = R.layout.fragment_register

    private lateinit var checkIcon: Drawable
    override val viewModel: RegisterViewModel by viewModels()
    // Constants
    private var selectedImageUri: Uri? = null

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, getLayoutResID(), container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbarmain)
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.create_your_account)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkIcon = ContextCompat.getDrawable(requireActivity(), R.drawable.baseline_check_24)!!


        addFocusListener()
        addTextWatcher()

        binding.btnRegister.setOnClickListener {
            viewModel.register()
        }
        setUpObserve()
        initListener()
        initViewModel()
    }
    private fun navigateToHome() {
//            requireActivity().startActivity(Intent(activity, HomeActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            })
//            requireActivity().finish()
//        }
        binding.btnRegister.setOnClickListener { v ->
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_homeActivity)
        }
    }
    private fun initListener(){
        binding.btnLoginAccount.setOnClickListener { v->
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.imagePerson.setOnClickListener{
            openGallery()
        }
    }

    //=============================================================================
    override fun setUpObserve() {
        viewModel.errorMessage.observe(viewLifecycleOwner){errorMessage->
            errorMessage?.let {
                view?.showSnakeBarError(errorMessage)
            }
        }
        viewModel.registerUser.observe(viewLifecycleOwner){user->
            user?.let {
               progressDialog.show()
            }
        }
    }
    private fun initViewModel(){
        lifecycleScope.launch {
            viewModel.registerState.collect{state->
                state.let { resource ->
                    when(resource){
                        is Resource.Loading->{
                            progressDialog.show()
                        }
                        is Resource.Success->{
                            progressDialog.dismiss()
                            navigateToHome()
                        }
                        is Resource.Error->{
                            progressDialog.dismiss()
                            val msg = resource.exception?.message?:getString(R.string.generic_error_msg)
                            Log.d(LoginFragment.TAG, "initViewModelError: $msg")
                            view?.showSnakeBarError(msg)
                        }
                    }
                }
            }
        }

    }

//=========================================================================================
/*This method open gallery and choose image then save in icon image. */
private fun openGallery() {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    startActivityForResult(intent, GALLERY_REQUEST_CODE)
}

    @OptIn(DelicateCoroutinesApi::class)
    private fun saveImageToStorage(imageUri: Uri) {
        GlobalScope.launch(Dispatchers.IO) {
            val imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
            val file = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_image.jpg")
            var success = false

            try {
                val outputStream = FileOutputStream(file)
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
                success = true
            } catch (e: IOException) {
                e.printStackTrace()
            }

            withContext(Dispatchers.Main) {
                if (success) {
                    // Image saved successfully
                    binding.imagePerson.setImageURI(imageUri)
                } else {
                    // Error saving image
                }
            }
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_REQUEST_CODE
                )
                return false
            }
        } else {
            return true
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, save the image again
                if (selectedImageUri != null) {
                    saveImageToStorage(selectedImageUri!!)
                }
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 1001
        private const val STORAGE_PERMISSION_REQUEST_CODE = 1002
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            if (selectedImage != null) {
                selectedImageUri = selectedImage
                if (isStoragePermissionGranted()) {
                    saveImageToStorage(selectedImageUri!!)
                }
            }
            val uri = data.data //filePath
            val storage = FirebaseStorage.getInstance()
            val time = Date().time
            val reference = storage.reference.child("ProfilePicture").child(time.toString() + "")
            reference.putFile(uri!!).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reference.downloadUrl.addOnCompleteListener { uri ->
                        val filePath = uri.toString()
                        val obj = HashMap<String, Any>()
                        obj["image"] = filePath
                        database.reference.child("users")
                            .child(FirebaseAuth.getInstance().uid!!).updateChildren(obj)
                            .addOnSuccessListener { }
                    }
                }
            }
            binding.imagePerson.setImageURI(data.data)
            selectedImageUri = data.data!!
        }
    }
    //==========================================================================================
    private fun addTextWatcher() {
        binding.edNameSign.addTextChangedListener(this)
        binding.edPhoneSign.addTextChangedListener(this)
        binding.edEmailSign.addTextChangedListener(this)
        binding.edPasswordSing.addTextChangedListener(this)
    }

    private fun addFocusListener() {
        nameFocusListener()
        phoneFocusListener()
        emailFocusListener()
        passwordFocusListener()
    }

    private fun nameFocusListener() {
        val nameValue = binding.edNameSign
        nameValue.setOnFocusChangeListener { _, hasFocuse ->
            if (!hasFocuse) {
                validateName()
            }
        }

    }

    private fun phoneFocusListener() {
        val phoneValue = binding.edPhoneSign
        phoneValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePhone()
            }
        }

    }

    private fun emailFocusListener() {
        val emailValue = binding.edEmailSign
        emailValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmail()
            }
        }
    }

    private fun passwordFocusListener() {
        val passwordValue = binding.edPasswordSing
        passwordValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePassword()
            }
        }
    }

    private fun validateName(): Boolean {
        val name = binding.edNameSign.text.toString().trim()
        if (!name.isNameValid()) {
            binding.nameTil.error = getString(R.string.name_is_require)
            binding.nameTil.endIconDrawable = null
        } else {
            binding.nameTil.apply {
                error = null
                endIconDrawable = checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return binding.nameTil.error == null

    }

    private fun validatePhone(): Boolean {
        val phone = binding.edPhoneSign.text.toString().trim()

        if (!phone.isPhoneValid()) {
            binding.phoneTil.error = getString(R.string.phone_number_is_require)
            binding.phoneTil.endIconDrawable = null

        } else {
            binding.phoneTil.apply {
                error = null
                endIconDrawable = checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return binding.phoneTil.error == null
    }

    private fun validateEmail(): Boolean {
        val email = binding.edEmailSign.text.toString().trim()
        if (!email.isEmailValid()) {
            binding.emailTil.error = getString(R.string.invalid_email_address)
            binding.emailTil.endIconDrawable = null
        } else {
            binding.emailTil.apply {
                error = null
                endIconDrawable = checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return binding.emailTil.error == null
    }


    private fun validatePassword(): Boolean {
        val password = binding.edPasswordSing.text.toString().trim()
        if (!password.isPasswordValid()) {
            binding.passwordTil.error = getString(R.string.oops_your_password_is_not_correct)
            binding.passwordTil.endIconDrawable = null
        } else {
            binding.passwordTil.apply {
                error = null
                endIconDrawable = checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return binding.passwordTil.error == null
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        binding.btnRegister.isEnabled =
            binding.edNameSign.text!!.trim().toString().isNotEmpty()
                    && binding.edPhoneSign.text!!.isNotEmpty()
        binding.edEmailSign.text!!.trim().toString().isNotEmpty()

                && binding.edPasswordSing.text.toString().isNotEmpty()
                && validateEmail()
                && validatePassword()
    }


}