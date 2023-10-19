package com.holeCode.novamoda.storage


import android.content.Intent
import android.net.Uri
import android.util.Log

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.holeCode.novamoda.pojo.RegisterBody
import com.holeCode.novamoda.pojo.UserFirebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.holeCode.novamoda.util.Result
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseAuthenticationManager : AppCompatActivity() {
    private val TAG = "FIREBASE_AUTH"
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val storeFire by lazy {
        FirebaseFirestore.getInstance()
    }
    private val dbFire by lazy {
        FirebaseDatabase.getInstance()
    }

    private val storage by lazy {
        FirebaseStorage.getInstance()
    }
    private val currentUserDocRef: DocumentReference
        get() = storeFire.document("users/${mAuth.currentUser?.uid.toString()}")

    //   private val currentUserDocRef:DocumentReference
//        get() = storeFire.collection("user").document(mAuth.currentUser?.uid.toString())
    private val currentUserStorageRef: StorageReference
        get() = storage.reference.child(FirebaseAuth.getInstance().currentUser?.uid.toString())

    suspend fun registerUserByFirebase(
        email: String,
        password: String
    ): Result<Boolean> {
        return try {
            mAuth.createUserWithEmailAndPassword(email, password).await()
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
                mAuth.sendPasswordResetEmail(email)
                when (val result = resetPasswordByFirebase(email)) {
                    is Result.Success -> {
                        val message = result.toString()
                        Toast.makeText(
                            this@FirebaseAuthenticationManager,
                            message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        // Open Gmail app
                        openGmail()
                    }

                    is Result.Error -> {
                        // Password reset email failed
                        val errorMessage = result.toString()
                        Toast.makeText(
                            this@FirebaseAuthenticationManager,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                openGmail()
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)

            }
        }

    //Add method to open gmail.
    private fun openGmail() {
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
}