package com.example.android.sparkstories.auth

import android.app.Activity
import androidx.fragment.app.Fragment

interface Authenticator {

    fun authenticateUser(fragment: Fragment)
    fun getUserId(): String
    fun isNotAuthenticated() : Boolean

}