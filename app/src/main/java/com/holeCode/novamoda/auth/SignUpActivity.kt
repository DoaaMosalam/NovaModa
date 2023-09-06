package com.holeCode.novamoda.auth

import android.Manifest
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.holeCode.novamoda.HomeScreenActivity
import com.holeCode.novamoda.R
import com.holeCode.novamoda.util.SetUpService
import com.holeCode.novamoda.pojo.User
import com.holeCode.novamoda.databinding.ActivitySignupBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SignUpActivity : AppCompatActivity(), TextWatcher {
    private lateinit var bindingSingUpActivity: ActivitySignupBinding
    private var selectedImageUri: Uri? = null
    private lateinit var checkIcon: Drawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSingUpActivity = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(bindingSingUpActivity.root)
        checkIcon = ContextCompat.getDrawable(this, R.drawable.baseline_check_24)!!
        //================================================================================================
        // this when text watcher button not clickable when full all edit tet
        bindingSingUpActivity.apply {
            edNameSign.addTextChangedListener(this@SignUpActivity)
            edPhoneSign.addTextChangedListener(this@SignUpActivity)
            edEmailSign.addTextChangedListener(this@SignUpActivity)
            edPasswordSing.addTextChangedListener(this@SignUpActivity)
        }

//        //================================================================================================
        nameFocusListener()
        phoneFocusListener()
        emailFocusListener()
        passwordFocusListener()
// handle on click button
        bindingSingUpActivity.apply {
            btnLoginAccount.setOnClickListener {
                navigationToLoginPage()
            }
            btnSignUp.setOnClickListener {
              registerUSer(
                    bindingSingUpActivity.edNameSign.text.toString()
                    ,bindingSingUpActivity.edPhoneSign.text.toString()
                    ,bindingSingUpActivity.edEmailSign.text.toString().trim()
                    ,bindingSingUpActivity.edPasswordSing.text.toString().trim()
                    ,bindingSingUpActivity.imagePerson.toString()
                )
            }
            imagePerson.setOnClickListener {
                openGallery()

            }
        }
    }

    //This method go to Login page.
    private fun navigationToLoginPage() {
        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
    }
    private fun  navigateToHomeScreen(){
//        val intent = Intent(this@SignUpActivity,HomeScreenActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        startActivity(intent)
        startActivity(Intent(this@SignUpActivity, HomeScreenActivity::class.java))
    }
// This method register user by api(name,phone,email,password)
  private fun registerUSer(name:String,phone:String,email: String,password:String,image:String){
    GlobalScope.launch(Dispatchers.IO) {
        val call = SetUpService.registerUSer(name, phone, email, password, image)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()
                if (user != null) {
                    // Registration successful, handle the response as needed
                    navigateToHomeScreen()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, t.message, Toast.LENGTH_SHORT).show()
                Log.i("TAG", "onFailure: " + t.message)
            }
        })
    }
  }

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

                    && validateName()
                    && validatePhone()
                    && validateEmail()
                    && validatePassword()

    }

    //==============================================================================================
    // this method on Focus Listener appear  (name,phone,email,password)
    private fun nameFocusListener() {
        val nameValue = bindingSingUpActivity.edNameSign
        nameValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateName()

            }
        }
    }

    private fun phoneFocusListener() {
        val phoneValue = bindingSingUpActivity.edPhoneSign
        phoneValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePhone()
            }
        }
    }

    private fun emailFocusListener() {
        val emailValue = bindingSingUpActivity.edEmailSign
        emailValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmail()
            }
        }
    }

    private fun passwordFocusListener() {
        val passwordValue = bindingSingUpActivity.edPasswordSing
        passwordValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePassword()
            }
        }
    }
    //=============================================================================================

    //this method to validate name when user register
    private fun validateName(): Boolean {
        val name = bindingSingUpActivity.edNameSign.text.toString().trim()
        if (name.isEmpty()) {
            bindingSingUpActivity.nameTil.error = "Name is required"
            bindingSingUpActivity.nameTil.endIconDrawable = null
        } else {
            bindingSingUpActivity.nameTil.apply {
                error = null
                endIconDrawable = checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }

        }
        return bindingSingUpActivity.nameTil.error == null
    }

    //this method to validate phone when user register
    private fun validatePhone(): Boolean {
        val phone = bindingSingUpActivity.edPhoneSign.text.toString().trim()

        if (phone.isEmpty()) {
            bindingSingUpActivity.phoneTil.error = "Phone number is required"
            bindingSingUpActivity.phoneTil.endIconDrawable = null
        } else {
            bindingSingUpActivity.phoneTil.apply {
                error=null
                endIconDrawable = checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return bindingSingUpActivity.phoneTil.error == null
    }

    //this method to validate email when user register
    private fun validateEmail(): Boolean {
        val email = bindingSingUpActivity.edEmailSign.text.toString().trim()

        if (email.isEmpty()) {
            bindingSingUpActivity.emailTil.error = "Email is required"
            bindingSingUpActivity.emailTil.endIconDrawable = null
        } else if (!isValidEmail(email)) {
            bindingSingUpActivity.emailTil.error = "Invalid email address"
            bindingSingUpActivity.emailTil.endIconDrawable = null
        } else {
            bindingSingUpActivity.emailTil.apply {
                error=null
//                endIconDrawable=checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return bindingSingUpActivity.emailTil.error == null
    }

    //this method to validate password when user register
    private fun validatePassword(): Boolean {
        val password = bindingSingUpActivity.edPasswordSing.text.toString().trim()

        if (password.isEmpty()) {
            bindingSingUpActivity.passwordTil.error = "Password is required"
            bindingSingUpActivity.passwordTil.endIconDrawable = null
        } else if (password.length < 6) {
            bindingSingUpActivity.passwordTil.error = "Password must be at least 6 characters"
            bindingSingUpActivity.passwordTil.endIconDrawable = null
        } else if (!password.matches(".*[A-Z].*".toRegex())) {
            bindingSingUpActivity.passwordTil.error =
                "Password must contain 1 upper-case character"
            bindingSingUpActivity.passwordTil.endIconDrawable = null
        } else if (!password.matches(".*[a-z].*".toRegex())) {
            bindingSingUpActivity.passwordTil.error =
                "Password must contain 1 lower-case character"
        } else if (!password.matches(".*[@#\$%^&+=].*".toRegex())) {
            bindingSingUpActivity.passwordTil.error =
                "Password must contain special[@#\$%^&+=] "
        } else {
            bindingSingUpActivity.passwordTil.apply {
                error = null
                endIconDrawable = checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
              setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return bindingSingUpActivity.passwordTil.error == null
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern = "[A-Z]+@[a-z]+\\.+[@#\$%^&+=]+"
        return password.matches(pattern.toRegex())
    }
//method pattern email
    private fun isValidEmail(email: String): Boolean {
        val pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(pattern.toRegex())
    }

    //===============================================================================================
    /*This method open gallery and choose image then save in icon image. */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }
    private fun saveImageToStorage(imageUri: Uri) {
        GlobalScope.launch(Dispatchers.IO) {
            val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_image.jpg")
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
                    bindingSingUpActivity.imagePerson.setImageURI(imageUri)
                } else {
                    // Error saving image
                }
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
