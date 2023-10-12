package com.holeCode.novamoda.storage

import android.content.Intent
import android.net.Uri
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.holeCode.novamoda.pojo.RegisterBody
import com.holeCode.novamoda.pojo.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirebaseAuthenticationManager :AppCompatActivity(){
    private val mAuth:FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val storeFire by lazy {
        FirebaseFirestore.getInstance()
    }
    private val dbFire by lazy {
        FirebaseDatabase.getInstance()
    }

    private val storage by lazy{
        FirebaseStorage.getInstance()
    }

    val currentUserDocRef : DocumentReference
        get() = storeFire.collection("user")
            .document(mAuth.currentUser?.uid.toString())
    val currentUserStorageRef:StorageReference
        get() = storage.reference.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
    fun registerUserFirebase(id:String,image:String,name:String,phone:String,email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                val newUSer = User(
                    id, image, name, phone, email, password)
                currentUserDocRef.set(newUSer)
                if (task.isSuccessful) {
                    val user: FirebaseUser? = mAuth.currentUser
                    onComplete(true, user?.uid)
                } else {
                    val errorMessage = task.exception?.message
                    onComplete(false, errorMessage)
                }
            }
    }



    private suspend fun saveUserProfile(user: RegisterBody) = withContext(Dispatchers.IO) {
        currentUserDocRef.set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@FirebaseAuthenticationManager, "Data inserted", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(
                        this@FirebaseAuthenticationManager,
                        "Failed to insert data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private suspend fun sendResetEmail(email: String)= withContext(Dispatchers.IO) {

        mAuth.sendPasswordResetEmail(email.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this@FirebaseAuthenticationManager, "Open Gmail", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    openGmail()
                }
            }

        }

    }

    //Add method to open gmail.
    private suspend fun openGmail() {
        lifecycleScope.launch {
            try {
                // Open Gmail app
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://mail.google.com")
                }

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            } catch (e: Exception) {
                Toast.makeText(this@FirebaseAuthenticationManager, e.message, Toast.LENGTH_SHORT).show()
            }

        }

    }


}