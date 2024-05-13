package com.holeCode.novamoda.ui.fragments.login

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.holeCode.novamoda.BuildConfig

fun getGoogleRequestIntent(context: Activity): Intent {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.clientServerId)
        .requestEmail()
        .requestProfile()
        .requestServerAuthCode(BuildConfig.clientServerId).build()

    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
    googleSignInClient.signOut()
    return googleSignInClient.signInIntent
}