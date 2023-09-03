package com.holeCode.novamoda.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.holeCode.novamoda.R
import com.holeCode.novamoda.databinding.ActivitySignupBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class SignUpActivity : AppCompatActivity(), TextWatcher,View.OnClickListener,View.OnKeyListener,View.OnFocusChangeListener {
    private lateinit var bindingSingUpActivity: ActivitySignupBinding
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSingUpActivity = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(bindingSingUpActivity.root)
        //================================================================================================
        // this when text watcher button not clickable when full all edit tet
        bindingSingUpActivity.edNameSign.addTextChangedListener(this@SignUpActivity)
        bindingSingUpActivity.edEmailSign.addTextChangedListener(this@SignUpActivity)
        bindingSingUpActivity.edPasswordSing.addTextChangedListener(this@SignUpActivity)
        bindingSingUpActivity.edPhoneSign.addTextChangedListener(this@SignUpActivity)
//        //================================================================================================
        bindingSingUpActivity.edNameSign.onFocusChangeListener = this
        bindingSingUpActivity.edPhoneSign.onFocusChangeListener = this
        bindingSingUpActivity.edEmailSign.onFocusChangeListener = this
        bindingSingUpActivity.edPasswordSing.onFocusChangeListener = this

//            nameFocusedListener()

        bindingSingUpActivity.btnLoginAccount.setOnClickListener {
            navigationToLoginPage()
        }

        bindingSingUpActivity.imagePerson.setOnClickListener {
            openGallery()
        }

    }

    //This method go to Login page.
    private fun navigationToLoginPage() {
        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
    }

    //================================================================================================
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        bindingSingUpActivity.btnSignUp.isEnabled =
            bindingSingUpActivity.edNameSign.text!!.trim().isNotEmpty()
                    && bindingSingUpActivity.edPhoneSign.text.toString().isNotEmpty()
                    && bindingSingUpActivity.edEmailSign.text!!.trim().isNotEmpty()
                    && bindingSingUpActivity.edPasswordSing.text!!.trim().isNotEmpty()
    }
    //==============================================================================================
//    private fun nameFocusedListener(){
//        bindingSingUpActivity.edNameSign.setOnFocusChangeListener{_,focused->
//            if (!focused){
//                bindingSingUpActivity.nameTil.helperText = validateFullName()
//            }
//        }
//
//    }

    private fun isValid(): Boolean {
        return validateFullName() && validatePhone() && validateEmail() && validatePassword()
    }
    private fun validateFullName(): Boolean {
        var errorMessage: String? = null
        val value = bindingSingUpActivity.edNameSign.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Name is required"
        }
        if (errorMessage != null) {
            bindingSingUpActivity.nameTil.apply {
                isErrorEnabled = true
                error == errorMessage
            }
        }
        return  errorMessage==null
    }

    private fun validatePhone(): Boolean {
        var errorMessage: String? = null
        val value = bindingSingUpActivity.edPhoneSign.text?.trim().toString()
        if (value.length < 12) {
            errorMessage = "The Phone numbers less than 12 digit"
        } else if (value.length > 12) {
            errorMessage = "The Phone numbers more than 12 digit"
        }
        if (errorMessage != null) {
            bindingSingUpActivity.phoneTil.apply {
                isErrorEnabled = true
                error == errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validateEmail(): Boolean {
        var errorMessage: String? = null
        val value = bindingSingUpActivity.edEmailSign.text.toString()
        if (value.isEmpty()){
            errorMessage = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Email is invalid"
        }
        if (errorMessage != null) {
            bindingSingUpActivity.emailTil.apply {
                isErrorEnabled = true
                error == errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validatePassword(): Boolean {
        var errorMessage: String? = null
        val value = bindingSingUpActivity.edPasswordSing.text.toString()
        if (value.length < 8) {
            errorMessage = "Password must be at least 6 characters long"
        }
        if (!value.matches(".*[A-Z].*".toRegex())) {
            errorMessage = "Password must contain 1 upper-case character"
        }
        if (!value.matches(".*[a-z].*".toRegex())) {
            errorMessage = "Password must contain 1 lower-case character"
        }
        if (!value.matches(".*[@#\$%^&+=].*".toRegex())) {
            errorMessage = "Password must contain special "
        }

        if (errorMessage != null) {
            bindingSingUpActivity.passwordTil.apply {
                isErrorEnabled = true
                error == errorMessage
            }
        }
        return errorMessage == null
    }


    override fun onClick(view: View?) {
    }

    override fun onFocusChange(view: View?, hasFocuse: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.ed_nameSign -> {
                    if (!hasFocuse) {
                        if (bindingSingUpActivity.nameTil.isErrorEnabled) {
                            bindingSingUpActivity.nameTil.isErrorEnabled = false
                        }
                    } else {
                        validateFullName()
                    }

                }

                R.id.ed_phoneSign -> {
                    if (hasFocuse) {
                        if (bindingSingUpActivity.phoneTil.isErrorEnabled) {
                            bindingSingUpActivity.phoneTil.isErrorEnabled = false
                        }

                    } else {
                        validatePhone()
                    }
                }

                R.id.ed_emailSign -> {
                    if (hasFocuse) {
                        if (bindingSingUpActivity.emailTil.isErrorEnabled) {
                            bindingSingUpActivity.emailTil.isErrorEnabled = false
                        }
                    } else {
                        validateEmail()
                    }
                }

                R.id.ed_PasswordSing -> {
                    if (hasFocuse) {
                        if (bindingSingUpActivity.passwordTil.isErrorEnabled) {
                            bindingSingUpActivity.passwordTil.isErrorEnabled = false
                        }
                    } else {
                        validatePassword()
                    }
                }
            }
        }
    }
    override fun onKey(view: View?, event: Int, keyEvent: KeyEvent?): Boolean {
        return false
    }



//===============================================================================================
private fun openGallery() {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    startActivityForResult(intent, GALLERY_REQUEST_CODE)
}

    private fun saveImageToStorage(imageUri: Uri) {
        if (isStoragePermissionGranted()) {
            val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            val outputStream: OutputStream?
            val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_image.jpg")
            try {
                outputStream = FileOutputStream(file)
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
                // Image saved successfully
                bindingSingUpActivity.imagePerson.setImageURI(imageUri)
            } catch (e: IOException) {
                e.printStackTrace()
                // Error saving image
            }
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_REQUEST_CODE
                )
                return false
            }
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
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
        }
    }
}