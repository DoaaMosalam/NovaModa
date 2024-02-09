package com.holeCode.novamoda.storage


import android.content.Intent
import android.net.Uri

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.holeCode.novamoda.pojo.UserFirebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.holeCode.novamoda.util.Result
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Firebasedb : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val storeFire by lazy {
        FirebaseFirestore.getInstance()
    }
    private val storage by lazy {
        FirebaseStorage.getInstance()
    }

    private val currentUserDocRef: DocumentReference
        get() = storeFire.document("users/${mAuth.currentUser?.uid.toString()}")
    private val currentUserStorageRef: StorageReference
        get() = storage.reference.child(mAuth.currentUser?.uid.toString())

    suspend fun registerUserByFirebase(
        email: String,
        password: String
    ): Result<Boolean> {
        return try {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                val newUser = UserFirebase(email, password)
                currentUserDocRef.set(newUser)
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@Firebasedb,
                        "Account Create Successful.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /*this method create login user by firebase make sure login */
    suspend fun loginUserByFirebase(email: String, password: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val credential = EmailAuthProvider.getCredential(email, password)
            try {
                mAuth.signInWithCredential(credential).await()
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    /*this method reset password and open Gmail*/
    suspend fun resetPasswordByFirebase(email: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                mAuth.sendPasswordResetEmail(email).await()
                openGmail()
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)
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
                Result.Error(e)
            }

        }
    }
//
//         val packageName = "com.google.android.gm" // Package name of the Gmail app

    // Check if the Gmail app is installed on the device
//         val packageManager = packageManager
//         val intent = packageManager.getLaunchIntentForPackage(packageName)
//         if (intent != null) {
//             // Gmail app is installed
//             startActivity(intent)
//         } else {
//             // Gmail app is not installed, open Gmail in a browser
//             val emailUri = Uri.parse("https://mail.google.com")
//             val browserIntent = Intent(Intent.ACTION_VIEW, emailUri)
//             startActivity(browserIntent)
//         }
//
}