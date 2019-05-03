package com.example.android.sparkstories.auth

import android.app.Activity
import androidx.fragment.app.Fragment
import com.example.android.sparkstories.MainActivity
import com.example.android.sparkstories.ui.splash.SplashFragment
import com.example.android.sparkstories.ui.splash.SplashFragment.Companion.RC_SIGN_IN
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class AuthenticatorImpl: Authenticator {

    private val  authUI = AuthUI.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.FacebookBuilder().build(),
        AuthUI.IdpConfig.TwitterBuilder().build()
    )

    override fun authenticateUser(fragment: Fragment) {
        fragment.startActivityForResult(
            authUI
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun getUserId(): String {
        val id = firebaseAuth.currentUser?.uid.toString()
        Timber.d("my test fetched id: ${id}")
        return id
    }

    override fun isNotAuthenticated() = firebaseAuth.currentUser == null

}
