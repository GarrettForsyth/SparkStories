package com.example.android.sparkstories.auth

import android.app.Activity
import com.example.android.sparkstories.MainActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class AuthenticatorImpl: Authenticator {

    private val  authUI = AuthUI.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.FacebookBuilder().build(),
        AuthUI.IdpConfig.TwitterBuilder().build()
    )

    override fun authenticateUser(activity: Activity) {
        activity.startActivityForResult(
            authUI
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            MainActivity.RC_SIGN_IN
        )
    }

    override fun getUserId() = firebaseAuth.currentUser?.uid.toString()



}
