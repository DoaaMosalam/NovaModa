package com.holeCode.novamoda.auth

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
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
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.holeCode.novamoda.HomeScreenActivity
import com.holeCode.novamoda.R
import com.holeCode.novamoda.data.ValidateEmailBody
import com.holeCode.novamoda.databinding.ActivitySignupBinding
import com.holeCode.novamoda.pojo.RegisterBody
import com.holeCode.novamoda.pojo.User
import com.holeCode.novamoda.repository.AuthRepository
import com.holeCode.novamoda.storage.FirebaseAuthenticationManager
import com.holeCode.novamoda.storage.SharedPreferencesManager
import com.holeCode.novamoda.util.APIService
import com.holeCode.novamoda.view_model.RegisterActivityViewModel
import com.holeCode.novamoda.view_model.RegisterActivityViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date
import java.util.UUID

class SignUpActivity : AppCompatActivity(), TextWatcher, View.OnClickListener, View.OnKeyListener {
    private lateinit var bindingSingUpActivity: ActivitySignupBinding

    private lateinit var checkIcon: Drawable
    private lateinit var mViewModel: RegisterActivityViewModel
    // Constants
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var selectedImage: Uri
    private lateinit var userName: String
    private lateinit var userstatus: String
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val storeFire by lazy {
        FirebaseFirestore.getInstance()
    }
    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    private val storage by lazy {
        FirebaseStorage.getInstance()
    }
    private val currentUserDocRef: DocumentReference
        get() = storeFire.collection("user").document(mAuth.currentUser?.uid.toString())
    private val currentUserStorageRef: StorageReference
        get() = storage.reference.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
    private var firebaseAuthenticationManager: FirebaseAuthenticationManager
    init {
        firebaseAuthenticationManager = FirebaseAuthenticationManager()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSingUpActivity = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(bindingSingUpActivity.root)
// After successful singUp
        SharedPreferencesManager(this).setUserIsRegistered(true)

// When user logs out or exits the app
        SharedPreferencesManager(this).setUserIsRegistered(false)

        //============================================================================================
        checkIcon = ContextCompat.getDrawable(this, R.drawable.baseline_check_24)!!
        // handle toolbar
        val toolbar = findViewById<TextView>(R.id.signUp)
        toolbar.text = "SignUp"
        setSupportActionBar(bindingSingUpActivity.toolbarmain)
        supportActionBar?.title = ""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //======================================================================================================
        // handle set on click listener.
        bindingSingUpActivity.btnLoginAccount.setOnClickListener(this)
        bindingSingUpActivity.btnSignUp.setOnClickListener(this)
        bindingSingUpActivity.imagePerson.setOnClickListener(this)

        mViewModel = ViewModelProvider(
            this,
            RegisterActivityViewModelFactory(AuthRepository(APIService.getService()), application)
        )
            .get(RegisterActivityViewModel::class.java)
//==================================================================================================

        setUpObserver()

//================================================================================================
        // this when text watcher button not clickable when full all edit tet
        bindingSingUpActivity.apply {
            edNameSign.addTextChangedListener(this@SignUpActivity)
            edPhoneSign.addTextChangedListener(this@SignUpActivity)
            edEmailSign.addTextChangedListener(this@SignUpActivity)
            edPasswordSing.addTextChangedListener(this@SignUpActivity)
        }

        //================================================================================================
        nameFocusListener()
        phoneFocusListener()
        emailFocusListener()
        passwordFocusListener()
    }//end onCreate

    //===============================================================================================
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    //==================================================================================================
    /*This method is inheritance from TextWatcher
    * inside method afterTextChange call methods editText that can be click button after full all text
    * and call method validateName && validatePhone && validateEmail & validatePassword */
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

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

    //=================================================================================================
    /*This method is inheritance from setOnClickListener. */
    override fun onClick(view: View?) {
        if(view != null){
        when(view.id){
                R.id.imagePerson->{
                    lifecycleScope.launch {
                        OpenImageChooser()
                    }
                }
                R.id.btn_signUp ->{
                    onSubmit()
                    lifecycleScope.launch(Dispatchers.Main) { uploadData() }
                }
                R.id.btn_LoginAccount ->{
                    startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))

                }
            }
        }
    }

    //=================================================================================================
    /*This method is inheritance from SetOnKey */
    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        if (KeyEvent.KEYCODE_ENTER == keyCode && keyEvent!!.action == KeyEvent.ACTION_UP) {
//            do register
            onSubmit()
        }
        return false
    }

    //=================================================================================================
// this method handle go to homeActivity.
    private fun navigateGoToHome() {
        startActivity(Intent(this@SignUpActivity, HomeScreenActivity::class.java))
    }

    //==============================================================================================
    private fun setUpObserver() {
        mViewModel.getIsLoading().observe(this) {
            bindingSingUpActivity.progressbar.isVisible = it
        }
//        mViewModel.getIsUniqueEmail().observe(this) {
//        if (validateEmail()){
//            if (it){
//                bindingSingUpActivity.emailTil.apply {
//                    if (isErrorEnabled)isErrorEnabled=false
//                    setStartIconDrawable(R.drawable.baseline_check_24)
//                    setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
//                }
//            }else{
//                bindingSingUpActivity.emailTil.apply {
//                    if (startIconDrawable!= null)startIconDrawable =null
//                    isErrorEnabled =true
//                    error("Email is already taken")
//                }
//            }
//        }
//        }
        mViewModel.getErrorMessage().observe(this) {
            //Name,Phone,Email,Password
            val formErrorKey = arrayOf("name", "phone", "email", "password")
            val message = StringBuilder()
            it.map { entry ->
                if (formErrorKey.contains(entry.key)) {
                    when (entry.key) {
                        "name" -> {
                            bindingSingUpActivity.nameTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }

                        }

                        "phone" -> {
                            bindingSingUpActivity.phoneTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "email" -> {
                            bindingSingUpActivity.emailTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "password" -> {
                            bindingSingUpActivity.passwordTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }
                    }
                } else {
                    message.append(entry.value).append("\n")
                }
                if (message.isNotEmpty()) {
                    AlertDialog.Builder(this)
                        .setIcon(R.drawable.baseline_info_24)
                        .setTitle("INFORMATION")
                        .setMessage(message)
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            }
        }
        mViewModel.getUser().observe(this) {
            if (it != null) {
                navigateGoToHome()
            }
        }
    }

    private fun onSubmit() {

        if (validate()) {
            //make Api request.
            mViewModel.registerUserVM(
                RegisterBody(
                    bindingSingUpActivity.imagePerson.toString(),
                    bindingSingUpActivity.edNameSign.text.toString(),
                    bindingSingUpActivity.edPhoneSign.text.toString(),
                    bindingSingUpActivity.edEmailSign.text.toString(),
                    bindingSingUpActivity.edPasswordSing.text.toString()
                )
            )
        }
        mViewModel.registerUserByFirebase(
            bindingSingUpActivity.edEmailSign.text.toString(),
            bindingSingUpActivity.edPasswordSing.text.toString()
        )


        val intent = Intent(this@SignUpActivity, HomeScreenActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    /*This method validate all field edit text */
    private fun validate(): Boolean {
        var isvalide = true
        if (!validateName()) isvalide = false
        if (!validatePhone()) isvalide = false
        if (!validateEmail(shouldUpdateView = false)) isvalide = false
        if (!validatePassword()) isvalide = false
        return isvalide

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
            } else if (!hasFocus) {
                mViewModel.validateEmailAddress(ValidateEmailBody(emailValue.toString()))
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

    //==================================================================================================
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
        } else if (!phone.matches("""^\d{10}$""".toRegex())) {
            bindingSingUpActivity.phoneTil.error = "Valid phone number"
        } else {
            bindingSingUpActivity.phoneTil.apply {
                error = null
                endIconDrawable = checkIcon
                setStartIconDrawable(R.drawable.baseline_check_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }
        return bindingSingUpActivity.phoneTil.error == null
    }

    //this method to validate email when user register
    private fun validateEmail(shouldUpdateView: Boolean = true): Boolean {
        val email = bindingSingUpActivity.edEmailSign.text.toString().trim()

        if (email.isEmpty()) {
            bindingSingUpActivity.emailTil.error = "Email is required"
            bindingSingUpActivity.emailTil.endIconDrawable = null
        } else if (!isValidEmail(email)) {
            bindingSingUpActivity.emailTil.error = "Invalid email address"
            bindingSingUpActivity.emailTil.endIconDrawable = null
        } else {
            bindingSingUpActivity.emailTil.apply {
                error = null
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
        } else if (password.length < 8) {
            bindingSingUpActivity.passwordTil.error = "Password must be at least 8 characters"
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
        } else if (!password.matches(".*[1-9]|10.*".toRegex())) {
            bindingSingUpActivity.passwordTil.error =
                "Password must contains numbers 1:10"
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

    //method pattern email
    private fun isValidEmail(email: String): Boolean {
        val pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(pattern.toRegex())
    }

    //===============================================================================================
    /*This method open gallery and choose image then save in icon image. */
    suspend fun OpenImageChooser() = withContext(Dispatchers.IO) {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))

        }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private suspend fun uploadData() = withContext(Dispatchers.IO) {
        val uid = mAuth.currentUser?.uid.toString()
        val name = bindingSingUpActivity.edNameSign.text.toString()
        val phone = bindingSingUpActivity.edPhoneSign.text.toString()
        val email = bindingSingUpActivity.edEmailSign.text.toString()
        val password = bindingSingUpActivity.edPasswordSing.text.toString()

        val user = User(uid, "No Image", name, phone,email,password)
        val reference = currentUserStorageRef.child("Profile").child(Date().time.toString())
        reference.putFile(selectedImage).addOnCompleteListener { uploadTask ->
            if (uploadTask.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { downloadUri ->
                    user.image = downloadUri.toString()
                    lifecycleScope.launch {
                        saveUserProfile(user)
                    }
                }
            } else {
                lifecycleScope.launch {
                    saveUserProfile(user)
                }

            }
        }

    }

    private suspend fun saveUserProfile(user: User) = withContext(Dispatchers.IO) {
        currentUserDocRef.set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@SignUpActivity, "Data inserted", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignUpActivity, HomeScreenActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Failed to insert data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    suspend fun uploadInfo(imageUri: String) = withContext((Dispatchers.IO)) {
        val user = User(
            mAuth.uid.toString(),
            imageUri,
            bindingSingUpActivity.edNameSign.text.toString(),
            bindingSingUpActivity.edPhoneSign.text.toString(),
            bindingSingUpActivity.edEmailSign.text.toString(),
            bindingSingUpActivity.edPasswordSing.text.toString()

        )
        database.reference.child("users")
            .child(mAuth.uid.toString())
            .setValue(user)
            .addOnCompleteListener {
                Toast.makeText(this@SignUpActivity, "Data  inserted", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this@SignUpActivity, HomeScreenActivity::class.java))
                finish()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (data.data != null) {
                /*this code compress image
                * save image in firebase realtime*/
                bindingSingUpActivity.imagePerson.setImageURI(data.data)
                val selectedImagePath = data.data
                val selectedImageBmp =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImagePath)
                val outPutStream = ByteArrayOutputStream()
                selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 20, outPutStream)
                val selectedImageByte = outPutStream.toByteArray()

//                UploadProfileImage(selectedImageByte) { path ->
//                    val userMap = mutableMapOf<String, Any>()
//                    userMap["name"] = userName
//                    userMap["Pictures"] = path
//                    currentUserDocRef.update(userMap)
//
//                }

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
            }
            bindingSingUpActivity.imagePerson.setImageURI(data.data)
            selectedImage = data.data!!

        }
    }


//    private fun UploadProfileImage(
//        selectedImageByte: ByteArray,
//        onSuccess: (imagePath: String) -> Unit
//    ) {
//        val ref =
//            currentUserStorageRef.child("ProfilePictures/${UUID.nameUUIDFromBytes(selectedImageByte)}")
//        ref.putBytes(selectedImageByte).addOnCompleteListener {
//            if (it.isSuccessful) {
//                onSuccess(ref.path)
//            } else {
//                Toast.makeText(
//                    this@ProfileActivity,
//                    "Error:${it.exception?.message.toString()}",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        bindingSingUpActivity=null
//    }
}


