package com.holeCode.novamoda.data.repository.auth

import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.holeCode.novamoda.data.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
):FirebaseAuthRepository {
    override suspend fun loginWithGoogle(idToken: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            authResult.user?.let { user ->
                emit(Resource.Success(user.uid))

            } ?: run {
                emit(Resource.Error(Exception("User not found")))
            }

        } catch (e: Exception) {
            emit(Resource.Error(e))

        }
    }

    override suspend fun loginWithFacebook(token: String): Flow<Resource<String>> =flow {
        try {
            emit(Resource.Loading())
            val credential = FacebookAuthProvider.getCredential(token)
            val authResult = auth.signInWithCredential(credential).await()
            authResult.user?.let { user->
                emit(Resource.Success(user.uid))
            }?: run {
                emit(Resource.Error(Exception("User not found")))
            }
        }catch(e:Exception) {
            emit(Resource.Error(e))
        }

    }

    override suspend fun sendUpdatePasswordEmail(email: String): Flow<Resource<String>> {
        return flow {
            try {
                emit(Resource.Loading())
                auth.sendPasswordResetEmail(email).await()
                emit(Resource.Success("Password reset email sent "))

            }catch (e:Exception){
                emit(Resource.Error(e)) //Emit error
            }
        }

    }

    override fun logout() {
        auth.signOut()
    }
    companion object {
        private const val TAG = "FirebaseAuthRepositoryI"
    }
}